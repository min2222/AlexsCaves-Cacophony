package com.min01.acc.capabilities;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.min01.acc.item.ACCItems;
import com.min01.acc.item.animation.IAnimatableItem;
import com.min01.acc.misc.SmoothAnimationState;
import com.min01.acc.network.ACCNetwork;
import com.min01.acc.network.UpdatePlayerAnimationPacket;
import com.min01.acc.util.ACCUtil;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.PacketDistributor;

public class PlayerAnimationCapabilityImpl implements IPlayerAnimationCapability
{
	public static final Capability<IPlayerAnimationCapability> PLAYER_ANIMATION = CapabilityManager.get(new CapabilityToken<>() {});
	
	private int animationTick;
	private int animationState;
	private int prevAnimationState;
	
	public final SmoothAnimationState radrifleFireAnimationState = new SmoothAnimationState(0.999F, 0.4F);
	public final SmoothAnimationState radrifleHoldAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState radrifleHoldNearWallAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState radrifleRunningAnimationState = new SmoothAnimationState(0.999F, 0.4F);
	public final SmoothAnimationState radrifleOverchargeFireAnimationState = new SmoothAnimationState(0.999F, 0.4F);
	public final SmoothAnimationState radrifleOverheatAnimationState = new SmoothAnimationState(0.999F, 0.4F);
	
	public final SmoothAnimationState raybladeDrawRightAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState raybladeSwingRightAnimationState = new SmoothAnimationState();
	
	public final SmoothAnimationState raybladeDrawLeftAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState raybladeSwingLeftAnimationState = new SmoothAnimationState();
	
	public final SmoothAnimationState railgunFireAnimationState = new SmoothAnimationState(0.999F, 0.4F);
	public final SmoothAnimationState railgunHoldAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState railgunHoldNearWallAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState railgunRunningAnimationState = new SmoothAnimationState(0.999F, 0.4F);
	public final SmoothAnimationState railgunReloadAnimationState = new SmoothAnimationState(0.999F, 0.4F);
	public final SmoothAnimationState railgunChargeAnimationState = new SmoothAnimationState(0.999F, 0.4F);
	
	private final Entity entity;
	
	public PlayerAnimationCapabilityImpl(Entity entity) 
	{
		this.entity = entity;
	}
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("AnimationTick", this.animationTick);
		nbt.putInt("AnimationState", this.animationState);
		nbt.putInt("PrevAnimationState", this.prevAnimationState);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		this.setAnimationTick(nbt.getInt("AnimationTick"));
		this.setAnimationState(nbt.getInt("AnimationState"));
		this.setPrevAnimationState(nbt.getInt("PrevAnimationState"));
	}

	@Override
	public void tick(LivingEntity entity) 
	{
		if(this.getAnimationState() == 1 && entity.isHolding(ACCItems.RAYBLADE.get()))
		{
			if(this.getAnimationTick() == 25)
			{
				float size = 1.5F;
				Vec3 lookPos = ACCUtil.getLookPos(new Vec2(entity.getXRot(), entity.getYHeadRot()), entity.getEyePosition(), 0, 0, 1.5F);
				AABB aabb = new AABB(-size, -size, -size, size, size, size).move(lookPos);
				List<LivingEntity> list = entity.level.getEntitiesOfClass(LivingEntity.class, aabb, t -> t != entity && !t.isAlliedTo(entity));
				list.forEach(t -> 
				{
					t.hurt(entity.damageSources().mobAttack(entity), entity.getRandom().nextInt(100, 200) + 1);
				});
			}
		}
		if(this.getAnimationTick() > 0)
		{
			if(!(entity.getItemInHand(entity.getUsedItemHand()).getItem() instanceof IAnimatableItem))
			{
				this.setAnimationTick(0);
			}
			this.setAnimationTick(this.getAnimationTick() - 1);
		}
		else
		{
			this.setAnimationState(0);
			this.setAnimationTick(0);
		}
		if(entity.level.isClientSide)
		{
			this.radrifleFireAnimationState.updateWhen(this.getAnimationState() == 1 && entity.isHolding(ACCItems.RADRIFLE.get()), entity.tickCount);
			this.radrifleHoldAnimationState.updateWhen(this.getAnimationState() == 0 && entity.isHolding(ACCItems.RADRIFLE.get()) && !entity.isSprinting() && !isLookingWall(entity), entity.tickCount);
			this.radrifleHoldNearWallAnimationState.updateWhen(this.getAnimationState() == 0 && entity.isHolding(ACCItems.RADRIFLE.get()) && !entity.isSprinting() && isLookingWall(entity), entity.tickCount);
			this.radrifleRunningAnimationState.updateWhen(this.getAnimationState() == 0 && entity.isHolding(ACCItems.RADRIFLE.get()) && entity.isSprinting(), entity.tickCount);
			this.radrifleOverchargeFireAnimationState.updateWhen(this.getAnimationState() == 3 && entity.isHolding(ACCItems.RADRIFLE.get()), entity.tickCount);
			this.radrifleOverheatAnimationState.updateWhen(this.getAnimationState() == 4 && entity.isHolding(ACCItems.RADRIFLE.get()), entity.tickCount);
			
			this.raybladeDrawRightAnimationState.updateWhen(this.getAnimationState() == 0 && entity.getMainHandItem().is(ACCItems.RAYBLADE.get()), entity.tickCount);
			this.raybladeSwingRightAnimationState.updateWhen(this.getAnimationState() == 1 && entity.getMainHandItem().is(ACCItems.RAYBLADE.get()), entity.tickCount);
			this.raybladeDrawLeftAnimationState.updateWhen(this.getAnimationState() == 0 && entity.getOffhandItem().is(ACCItems.RAYBLADE.get()), entity.tickCount);
			this.raybladeSwingLeftAnimationState.updateWhen(this.getAnimationState() == 1 && entity.getOffhandItem().is(ACCItems.RAYBLADE.get()), entity.tickCount);
			
			this.railgunFireAnimationState.updateWhen(this.getAnimationState() == 1 && entity.isHolding(ACCItems.MAGNETIC_RAILGUN.get()), entity.tickCount);
			this.railgunHoldAnimationState.updateWhen(this.getAnimationState() == 0 && entity.isHolding(ACCItems.MAGNETIC_RAILGUN.get()) && (!entity.isSprinting() || ACCUtil.getOwner(entity) != null) && !isLookingWall(entity), entity.tickCount);
			this.railgunHoldNearWallAnimationState.updateWhen(this.getAnimationState() == 0 && entity.isHolding(ACCItems.MAGNETIC_RAILGUN.get()) && (!entity.isSprinting() || ACCUtil.getOwner(entity) != null) && isLookingWall(entity), entity.tickCount);
			this.railgunRunningAnimationState.updateWhen(this.getAnimationState() == 0 && entity.isHolding(ACCItems.MAGNETIC_RAILGUN.get()) && entity.isSprinting() && ACCUtil.getOwner(entity) == null, entity.tickCount);
			this.railgunChargeAnimationState.updateWhen(this.getAnimationState() == 2 && entity.isHolding(ACCItems.MAGNETIC_RAILGUN.get()), entity.tickCount);
			this.railgunReloadAnimationState.updateWhen(this.getAnimationState() == 3 && entity.isHolding(ACCItems.MAGNETIC_RAILGUN.get()), entity.tickCount);
		}
		else
		{
			this.sendUpdatePacket();
		}
	}
	
	public static boolean isLookingWall(Entity entity)
	{
		Vec3 lookPos = ACCUtil.getLookPos(new Vec2(entity.getXRot(), entity.getYHeadRot()), entity.getEyePosition(), 0, 0, 1);
		HitResult result = entity.level.clip(new ClipContext(entity.getEyePosition(), lookPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity));
		return result.getType() == Type.BLOCK;
	}

	@Override
	public void setAnimationState(int state) 
	{
		this.animationState = state;
	}

	@Override
	public int getAnimationState() 
	{
		return this.animationState;
	}
	
	@Override
	public void setPrevAnimationState(int state) 
	{
		this.prevAnimationState = state;
	}

	@Override
	public int getPrevAnimationState() 
	{
		return this.prevAnimationState;
	}
	
	@Override
	public void setAnimationTick(int tick) 
	{
		this.animationTick = tick;
	}
	
	@Override
	public int getAnimationTick() 
	{
		return this.animationTick;
	}
	
	private void sendUpdatePacket() 
	{
		if(!this.entity.level.isClientSide)
		{
			ACCNetwork.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this.entity), new UpdatePlayerAnimationPacket(this.entity.getUUID(), this.animationState, this.prevAnimationState, this.animationTick));
		}
	}
	
	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) 
	{
		return PLAYER_ANIMATION.orEmpty(cap, LazyOptional.of(() -> this));
	}
}
