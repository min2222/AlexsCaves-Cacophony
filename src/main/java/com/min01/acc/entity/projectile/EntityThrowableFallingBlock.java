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
	
	public EntityThrowableFallingBlock(EntityType<? extends ThrowableProjectile> p_37466_, Level p_37467_)
	{
		super(p_37466_, p_37467_);
	}

	public EntityThrowableFallingBlock(EntityType<? extends ThrowableProjectile> p_37456_, double p_37457_, double p_37458_, double p_37459_, Level p_37460_) 
	{
		super(p_37456_, p_37457_, p_37458_, p_37459_, p_37460_);
	}

	public EntityThrowableFallingBlock(EntityType<? extends ThrowableProjectile> p_37462_, LivingEntity p_37463_, Level p_37464_)
	{
		super(p_37462_, p_37463_, p_37464_);
	}

	@Override
	protected void defineSynchedData()
	{
		this.entityData.define(BLOCK_STATE, Blocks.SAND.defaultBlockState());
	}
	
	@Override
	protected void onHitBlock(BlockHitResult p_37258_)
	{
		super.onHitBlock(p_37258_);
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
	protected void onHit(HitResult p_37260_)
	{
		super.onHit(p_37260_);
		Vec3 pos = p_37260_.getLocation();
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
	protected void onHitEntity(EntityHitResult p_37259_) 
	{
		super.onHitEntity(p_37259_);
		BlockState state = this.getBlockState();
		if(!state.isAir() && !(state.getBlock() instanceof TntBlock) && !(state.getBlock() instanceof NuclearBombBlock))
		{
			Entity entity = p_37259_.getEntity();
			if(entity.hurt(this.damageSources().thrown(this, this.getOwner()), 6.0F))
			{
				this.level.broadcastEntityEvent(this, (byte) 99);
				this.playSound(state.getSoundType().getBreakSound());
				this.discard();
			}
		}
	}
	
	@Override
	public void handleEntityEvent(byte p_19882_) 
	{
		super.handleEntityEvent(p_19882_);
		if(p_19882_ == 99)
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
		if(this.getBlockState().isAir()) 
		{
			this.discard();
		}
		this.move(MoverType.SELF, this.getDeltaMovement());
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundTag p_37265_)
	{
		super.addAdditionalSaveData(p_37265_);
		p_37265_.put("BlockState", NbtUtils.writeBlockState(this.getBlockState()));
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag p_37262_)
	{
		super.readAdditionalSaveData(p_37262_);
		this.setBlockState(NbtUtils.readBlockState(this.level.holderLookup(Registries.BLOCK), p_37262_.getCompound("BlockState")));
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
