package com.min01.acc.entity.projectile;

import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.min01.acc.entity.ACCEntities;
import com.min01.acc.item.ACCItems;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class EntityFearArrow extends AbstractArrow
{
	public EntityFearArrow(EntityType<? extends AbstractArrow> p_36858_, Level p_36859_)
	{
		super(p_36858_, p_36859_);
	}

	public EntityFearArrow(Level p_36861_, double p_36862_, double p_36863_, double p_36864_)
	{
		super(ACCEntities.FEAR_ARROW.get(), p_36862_, p_36863_, p_36864_, p_36861_);
	}

	public EntityFearArrow(Level p_36866_, LivingEntity p_36867_) 
	{
		super(ACCEntities.FEAR_ARROW.get(), p_36867_, p_36866_);
	}

	@Override
	protected ItemStack getPickupItem() 
	{
		return new ItemStack(ACCItems.ARROW_OF_FEAR.get());
	}
	
	@Override
	protected void onHitEntity(EntityHitResult p_36757_)
	{
		super.onHitEntity(p_36757_);
		Entity entity = p_36757_.getEntity();
        if(entity instanceof PathfinderMob mob && (!(mob instanceof TamableAnimal) || !((TamableAnimal) mob).isInSittingPose())) 
        {
        	if(!mob.getType().is(ACTagRegistry.RESISTS_TREMORSAURUS_ROAR))
        	{
                mob.setTarget(null);
                mob.setLastHurtByMob(null);
                if(mob.onGround())
                {
                    Vec3 randomShake = new Vec3(this.level.random.nextFloat() - 0.5F, 0, this.level.random.nextFloat() - 0.5F).scale(0.1F);
                    mob.setDeltaMovement(mob.getDeltaMovement().multiply(0.7F, 1, 0.7F).add(randomShake));
                }
                Vec3 vec = LandRandomPos.getPosAway(mob, 16, 7, this.position());
                if(vec != null)
                {
                    mob.getNavigation().moveTo(vec.x, vec.y, vec.z, 2.0D);
                }
        	}
        }
	}
}
