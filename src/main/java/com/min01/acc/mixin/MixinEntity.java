package com.min01.acc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.min01.acc.item.ACCItems;
import com.min01.acc.item.MagneticRailgunItem;
import com.min01.acc.util.ACCUtil;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

@Mixin(Entity.class)
public class MixinEntity
{
    @Inject(at = @At("TAIL"), method = "tick")
    private void tick(CallbackInfo ci)
    {
    	Entity entity = Entity.class.cast(this);
    	Entity owner = ACCUtil.getOwner(entity);
    	ACCUtil.tickOwner(entity);
    	
    	if(owner instanceof LivingEntity living && !(entity instanceof Player))
    	{
    		if(!living.isHolding(ACCItems.MAGNETIC_RAILGUN.get()))
    		{
    			ItemStack stack = living.getItemInHand(InteractionHand.MAIN_HAND);
    			MagneticRailgunItem.release(living, entity, stack);
    		}
    		else
    		{
    			Vec3 lookPos = ACCUtil.getLookPos(new Vec2(living.getXRot(), living.getYHeadRot()), living.getEyePosition(), 0.0F, 0.0F, 5.0F);
    			HitResult hitResult = living.level.clip(new ClipContext(living.getEyePosition(), lookPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, living));
    			Vec3 pos = hitResult.getLocation();
    			entity.teleportTo(pos.x, pos.y, pos.z);
    		}
    	}
    }
}
