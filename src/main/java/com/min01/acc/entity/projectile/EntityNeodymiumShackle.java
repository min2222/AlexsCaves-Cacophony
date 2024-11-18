package com.min01.acc.entity.projectile;

import com.min01.acc.entity.ACCEntities;
import com.min01.acc.item.ACCItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class EntityNeodymiumShackle extends ThrowableProjectile
{
	public static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(EntityNeodymiumShackle.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> ANIMATION_TICK = SynchedEntityData.defineId(EntityNeodymiumShackle.class, EntityDataSerializers.INT);
	
	public AnimationState loopAnimationState = new AnimationState();
	public AnimationState deployAnimationState = new AnimationState();
	public AnimationState impactAnimationState = new AnimationState();
	public AnimationState impactCloseAnimationState = new AnimationState();
	
	public ItemStack shackleItem = new ItemStack(ACCItems.NEODYMIUM_SHACKLE.get());
	
	public AbstractArrow.Pickup pickup = AbstractArrow.Pickup.ALLOWED;
	
	public EntityNeodymiumShackle(EntityType<? extends ThrowableProjectile> p_37466_, Level p_37467_)
	{
		super(p_37466_, p_37467_);
	}

	public EntityNeodymiumShackle(EntityType<? extends ThrowableProjectile> p_37456_, double p_37457_, double p_37458_, double p_37459_, Level p_37460_) 
	{
		super(p_37456_, p_37457_, p_37458_, p_37459_, p_37460_);
	}

	public EntityNeodymiumShackle(EntityType<? extends ThrowableProjectile> p_37462_, LivingEntity p_37463_, Level p_37464_)
	{
		super(p_37462_, p_37463_, p_37464_);
	}
	
	public EntityNeodymiumShackle(Level p_37569_, LivingEntity p_37570_, ItemStack p_37571_)
	{
		super(ACCEntities.NEODYMIUM_SHACKLE.get(), p_37570_, p_37569_);
		this.shackleItem = p_37571_.copy();
	}

	@Override
	protected void defineSynchedData() 
	{
		this.entityData.define(ANIMATION_STATE, 0);
		this.entityData.define(ANIMATION_TICK, 0);
	}
	
	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> p_219422_) 
	{
        if(ANIMATION_STATE.equals(p_219422_) && this.level.isClientSide) 
        {
            switch(this.getAnimationState()) 
            {
        		case 0: 
        		{
        			this.stopAllAnimationStates();
        			break;
        		}
        		case 1: 
        		{
        			this.stopAllAnimationStates();
        			this.deployAnimationState.start(this.tickCount);
        			break;
        		}
        		case 2: 
        		{
        			this.stopAllAnimationStates();
        			this.impactAnimationState.start(this.tickCount);
        			break;
        		}
        		case 3: 
        		{
        			this.stopAllAnimationStates();
        			this.impactCloseAnimationState.start(this.tickCount);
        			break;
        		}
            }
        }
	}
	
	public void stopAllAnimationStates() 
	{
		this.deployAnimationState.stop();
		this.impactAnimationState.stop();
		this.impactCloseAnimationState.stop();
	}
	
	@Override
	public boolean displayFireAnimation()
	{
		return false;
	}
	
	@Override
	public void tick()
	{
		super.tick();
		if(this.level.isClientSide)
		{
			this.loopAnimationState.startIfStopped(this.tickCount);
		}
		
		if(this.getAnimationState() == 1 && this.getAnimationTick() <= 0)
		{
			this.setAnimationState(0);
		}

		if(this.getAnimationState() == 2 && this.getAnimationTick() <= 0)
		{
			this.setAnimationState(0);
			this.spawnItem();
		}
		
		if(this.getAnimationTick() > 0)
		{
			this.setAnimationTick(this.getAnimationTick() - 1);
		}
	}
	
	public void spawnItem()
	{
		if(this.pickup != AbstractArrow.Pickup.DISALLOWED)
		{
	        this.spawnAtLocation(this.shackleItem.copy(), 0.1F);
		}
        this.discard();
	}
	
	@Override
	protected void onHitBlock(BlockHitResult p_37258_) 
	{
		super.onHitBlock(p_37258_);
		if(this.getAnimationState() != 2)
		{
			this.setAnimationState(2);
			this.setAnimationTick(6);
			this.setNoGravity(true);
			this.setDeltaMovement(Vec3.ZERO);
		}
	}
	
	@Override
	protected void onHitEntity(EntityHitResult p_37259_) 
	{
		super.onHitEntity(p_37259_);
		if(this.getOwner() != null && this.getAnimationState() != 2)
		{
			Entity entity = p_37259_.getEntity();
			if(this.getOwner() instanceof LivingEntity living)
			{
				if(entity.hurt(living.damageSources().mobAttack(living), 10.0F))
				{
					this.setAnimationState(2);
					this.setAnimationTick(6);
					this.setNoGravity(true);
					this.setDeltaMovement(Vec3.ZERO);
				}
			}
		}
	}
	
	@Override
	protected void addAdditionalSaveData(CompoundTag p_37265_) 
	{
		super.addAdditionalSaveData(p_37265_);
		p_37265_.putByte("Pickup", (byte)this.pickup.ordinal());
		p_37265_.put("Shackle", this.shackleItem.save(new CompoundTag()));
	}
	
	@Override
	protected void readAdditionalSaveData(CompoundTag p_37262_)
	{
		super.readAdditionalSaveData(p_37262_);
		this.pickup = AbstractArrow.Pickup.byOrdinal(p_37262_.getByte("Pickup"));
		if(p_37262_.contains("Shackle", 10))
		{
			this.shackleItem = ItemStack.of(p_37262_.getCompound("Shackle"));
		}
	}
    
	public void setAnimationTick(int value)
    {
        this.entityData.set(ANIMATION_TICK, value);
    }
    
    public int getAnimationTick()
    {
        return this.entityData.get(ANIMATION_TICK);
    }
	
    public void setAnimationState(int value)
    {
        this.entityData.set(ANIMATION_STATE, value);
    }
    
    public int getAnimationState()
    {
        return this.entityData.get(ANIMATION_STATE);
    }
}
