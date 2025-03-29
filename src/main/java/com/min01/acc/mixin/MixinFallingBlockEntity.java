package com.min01.acc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.min01.acc.capabilities.ACCCapabilities;
import com.min01.acc.item.ACCItems;
import com.min01.acc.util.ACCUtil;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

@Mixin(FallingBlockEntity.class)
public class MixinFallingBlockEntity
{
	@Inject(at = @At(value = "HEAD"), method = "tick")
	private void tick(CallbackInfo ci)
	{
		Entity entity = Entity.class.cast(this);
		entity.getCapability(ACCCapabilities.OWNER).ifPresent(t -> 
		{
			Entity owner = t.getOwner();
			if(owner instanceof Player player)
			{
	    		if(player.isHolding(ACCItems.MAGNETIC_RAILGUN.get()))
	    		{
	    			Vec3 lookPos = ACCUtil.getLookPos(new Vec2(player.getXRot(), player.getYHeadRot()), player.getEyePosition(), 0.0F, 0.0F, 5.0F);
	    			HitResult hitResult = entity.level.clip(new ClipContext(player.getEyePosition(), lookPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
	    			Vec3 pos = hitResult.getLocation();
	    			entity.moveTo(pos);
	    			entity.setOnGround(false);
	    		}
	    		else
	    		{
	    			entity.setNoGravity(false);
	    			t.setOwner(null);
	    		}
			}
		});
	}
}
