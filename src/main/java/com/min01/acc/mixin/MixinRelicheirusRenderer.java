package com.min01.acc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.alexmodguy.alexscaves.client.render.entity.RelicheirusRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.RelicheirusEntity;
import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.resources.ResourceLocation;

@Mixin(RelicheirusRenderer.class)
public class MixinRelicheirusRenderer
{
    @Inject(at = @At("HEAD"), method = "getTextureLocation", cancellable = true, remap = false)
    private void getTextureLocation(RelicheirusEntity entity, CallbackInfoReturnable<ResourceLocation> cir)
    {
    	if(entity.getPersistentData().contains("isPainted"))
    	{
    		cir.setReturnValue(new ResourceLocation(AlexsCavesCacophony.MODID, "textures/entity/relicheirus_painted.png"));
    	}
    }
}
