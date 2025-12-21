package com.min01.acc.entity.projectile;

import com.github.alexmodguy.alexscaves.server.block.NuclearBombBlock;
import com.github.alexmodguy.alexscaves.server.entity.item.NuclearBombEntity;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EntityThrowableFallingBlock extends ThrowableProjectile
{
	public static final EntityDataAccessor<BlockState> BLOCK_STATE = SynchedEntityData.defineId(EntityThrowableFallingBlock.class, EntityDataSerializers.BLOCK_STATE);
	
	public EntityThrowableFallingBlock(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
	}

	public EntityThrowableFallingBlock(EntityType<? extends ThrowableProjectile> pEntityType, double pX, double pY, double pZ, Level pLevel) 
	{
		super(pEntityType, pX, pY, pZ, pLevel);
	}

	public EntityThrowableFallingBlock(EntityType<? extends ThrowableProjectile> pEntityType, LivingEntity pShooter, Level pLevel)
	{
		super(pEntityType, pShooter, pLevel);
	}

	@Override
	protected void defineSynchedData()
	{
		this.entityData.define(BLOCK_STATE, Blocks.SAND.defaultBlockState());
	}
	
	@Override
	protected void onHitBlock(BlockHitResult pResult)
	{
		super.onHitBlock(pResult);
		BlockState state = this.getBlockState();
		if(!state.isAir() && !(state.getBlock() instanceof TntBlock) && !(state.getBlock() instanceof NuclearBombBlock))
		{
			if(!this.level.isClientSide)
			{
				this.level.setBlockAndUpdate(this.blockPosition(), state);
			}
			this.playSound(state.getSoundType().getPlaceSound());
			this.discard();
		}
	}
	
	@Override
	protected void onHit(HitResult pResult)
	{
		super.onHit(pResult);
		Vec3 pos = pResult.getLocation();
		BlockState state = this.getBlockState();
		if(state.getBlock() instanceof TntBlock)
		{
			PrimedTnt tnt = new PrimedTnt(this.level, pos.x, pos.y, pos.z, (LivingEntity) this.getOwner());
			tnt.setFuse(0);
			this.level.addFreshEntity(tnt);
			this.discard();
		}
		if(state.getBlock() instanceof NuclearBombBlock)
		{
			NuclearBombEntity tnt = new NuclearBombEntity(this.level, pos.x, pos.y, pos.z);
			tnt.setTime(NuclearBombEntity.MAX_TIME);
			this.level.addFreshEntity(tnt);
			this.discard();
		}
	}
	
	@Override
	protected void onHitEntity(EntityHitResult pResult) 
	{
		super.onHitEntity(pResult);
		BlockState state = this.getBlockState();
		if(!state.isAir() && !(state.getBlock() instanceof TntBlock) && !(state.getBlock() instanceof NuclearBombBlock))
		{
			Entity entity = pResult.getEntity();
			if(entity.hurt(this.damageSources().thrown(this, this.getOwner()), 6.0F))
			{
				this.level.broadcastEntityEvent(this, (byte) 99);
				this.playSound(state.getSoundType().getBreakSound());
				this.discard();
			}
		}
	}
	
	@Override
	public void handleEntityEvent(byte pId) 
	{
		super.handleEntityEvent(pId);
		if(pId == 99)
		{
			for(int i = 0; i < 60; ++i) 
			{
				this.level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, this.getBlockState()), this.getX(), this.getY(), this.getZ(), 0, 0, 0);
			}
		}
	}
	
	@Override
	public boolean isPickable()
	{
		return !this.isRemoved();
	}
	
	@Override
	public void tick() 
	{
		super.tick();
		BlockState state = this.getBlockState();
		if(state.isAir()) 
		{
			this.discard();
		}
		else if(this.verticalCollision || this.horizontalCollision || this.verticalCollisionBelow)
		{
			if(!(state.getBlock() instanceof TntBlock) && !(state.getBlock() instanceof NuclearBombBlock))
			{
				if(!this.level.isClientSide)
				{
					this.level.setBlockAndUpdate(this.blockPosition(), state);
				}
				this.playSound(state.getSoundType().getPlaceSound());
				this.discard();
			}
		}
		this.move(MoverType.SELF, this.getDeltaMovement());
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundTag pCompound)
	{
		super.addAdditionalSaveData(pCompound);
		pCompound.put("BlockState", NbtUtils.writeBlockState(this.getBlockState()));
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag pCompound)
	{
		super.readAdditionalSaveData(pCompound);
		this.setBlockState(NbtUtils.readBlockState(this.level.holderLookup(Registries.BLOCK), pCompound.getCompound("BlockState")));
	}
	
	public void setBlockState(BlockState state)
	{
		this.entityData.set(BLOCK_STATE, state);
	}
	
	public BlockState getBlockState()
	{
		return this.entityData.get(BLOCK_STATE);
	}
}
