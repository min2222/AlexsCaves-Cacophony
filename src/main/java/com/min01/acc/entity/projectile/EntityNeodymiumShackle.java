package com.min01.acc.entity.projectile;

import java.util.List;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.message.UpdateEffectVisualityEntityMessage;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.min01.acc.enchantment.ACCEnchantments;
import com.min01.acc.entity.ACCEntities;
import com.min01.acc.item.ACCItems;
import com.min01.acc.misc.SmoothAnimationState;
import com.min01.acc.util.ACCUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractArrow.Pickup;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class EntityNeodymiumShackle extends ThrowableProjectile
{
	public static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(EntityNeodymiumShackle.class, EntityDataSerializers.INT);
	public static final EntityDataAccessor<Integer> ANIMATION_TICK = SynchedEntityData.defineId(EntityNeodymiumShackle.class, EntityDataSerializers.INT);
	
	public final SmoothAnimationState loopAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState deployAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState impactAnimationState = new SmoothAnimationState();
	public final SmoothAnimationState impactCloseAnimationState = new SmoothAnimationState();
	
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
			this.loopAnimationState.updateWhen(true, this.tickCount);
			this.deployAnimationState.updateWhen(this.getAnimationState() == 1, this.tickCount);
			this.impactAnimationState.updateWhen(this.getAnimationState() == 2, this.tickCount);
			this.impactCloseAnimationState.updateWhen(this.getAnimationState() == 3, this.tickCount);
		}
		
		if(this.getAnimationState() != 3 && this.isOrbiting())
		{
			if(this.tickCount >= 300)
			{
				this.setAnimationState(3);
				this.setAnimationTick(20);
				this.setDeltaMovement(Vec3.ZERO);
			}
			else if(this.getOwner() != null)
			{
				int orbit = this.shackleItem.getEnchantmentLevel(ACCEnchantments.ORBITING.get());
				Entity owner = this.getOwner();
				Vec3 lookPos = ACCUtil.getLookPos(new Vec2(0, this.getYRot()), owner.getEyePosition().subtract(0.0F, 0.5F, 0.0F), 0, 0, 4 + orbit);
				this.setDeltaMovement(ACCUtil.fromToVector(this.position(), lookPos, 1.0F * orbit));
				this.setYRot(this.getYRot() + (15 * orbit));
			}
		}

		if(this.getAnimationState() == 3 && this.getAnimationTick() <= 0)
		{
			this.setAnimationState(0);
			if(this.isOrbiting())
			{
				if(this.getOwner() instanceof Player player && this.pickup != Pickup.DISALLOWED)
				{
					player.getInventory().add(this.shackleItem.copy());
				}
		        this.discard();
			}
			else
			{
				this.spawnItem();
			}
		}
		
		if(this.getAnimationTick() > 0)
		{
			this.setAnimationTick(this.getAnimationTick() - 1);
		}
	}
	
	@Override
	protected void updateRotation() 
	{
		
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
		if(this.getAnimationState() != 3 && !this.isOrbiting())
		{
			this.setAnimationState(3);
			this.setAnimationTick(20);
			this.setNoGravity(true);
			this.setDeltaMovement(Vec3.ZERO);
		}
	}
	
	@Override
	protected void onHitEntity(EntityHitResult p_37259_) 
	{
		super.onHitEntity(p_37259_);
		Entity entity = p_37259_.getEntity();
		Vec3 pos = p_37259_.getLocation();
		if(entity instanceof LivingEntity victim)
		{
			if(this.getOwner() instanceof LivingEntity living && this.getAnimationState() != 3)
			{
				if(entity.hurt(living.damageSources().thrown(this, living), 10.0F))
				{
					MobEffectInstance instance = new MobEffectInstance(ACEffectRegistry.STUNNED.get(), 40, 0);
					if(!entity.level.isClientSide && victim.addEffect(instance))
					{
		                AlexsCaves.sendMSGToAll(new UpdateEffectVisualityEntityMessage(entity.getId(), living.getId(), 3, instance.getDuration()));
					}
					if(!this.isOrbiting())
					{
						this.setAnimationState(3);
						this.setAnimationTick(20);
						this.setNoGravity(true);
						this.setDeltaMovement(Vec3.ZERO);
					}
					if(this.isVoltage())
					{
						LightningBolt bolt = new LightningBolt(EntityType.LIGHTNING_BOLT, this.level);
						bolt.setPos(pos);
						this.level.addFreshEntity(bolt);
					}
					if(this.isImpact())
					{
						int impact = this.shackleItem.getEnchantmentLevel(ACCEnchantments.IMPACT.get());
						this.playSound(SoundEvents.GENERIC_EXPLODE);
						List<LivingEntity> list = this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(impact * 3.0F), t -> t != this.getOwner() && !t.isAlliedTo(this));
						list.forEach(t -> 
						{
							if(!t.level.isClientSide && t.addEffect(instance))
							{
				                AlexsCaves.sendMSGToAll(new UpdateEffectVisualityEntityMessage(t.getId(), living.getId(), 3, instance.getDuration()));
							}
						});
					}
				}
			}
		}
	}
	
	public boolean isOrbiting()
	{
		return this.shackleItem.getEnchantmentLevel(ACCEnchantments.ORBITING.get()) > 0;
	}
	
	public boolean isVoltage()
	{
		return this.shackleItem.getEnchantmentLevel(ACCEnchantments.VOLTAGE.get()) > 0;
	}
	
	public boolean isImpact()
	{
		return this.shackleItem.getEnchantmentLevel(ACCEnchantments.IMPACT.get()) > 0;
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
