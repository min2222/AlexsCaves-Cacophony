package com.min01.acc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.alexmodguy.alexscaves.client.render.entity.VallumraptorRenderer;
import com.github.alexmodguy.alexscaves.server.entity.living.VallumraptorEntity;
import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.resources.ResourceLocation;

@Mixin(VallumraptorRenderer.class)
public class MixinVallumraptorRenderer
{
    @Inject(at = @At("HEAD"), method = "getTextureLocation", cancellable = true, remap = false)
    private void getTextureLocation(VallumraptorEntity entity, CallbackInfoReturnable<ResourceLocation> cir)
    {
    	if(entity.getPersistentData().contains("isPainted"))
    	{
    		if(entity.isElder())
    		{
        		cir.setReturnValue(new ResourceLocation(AlexsCavesCacophony.MODID, "textures/entity/vallumraptor_elder_painted.png"));
    		}
    		else
    		{
        		cir.setReturnValue(new ResourceLocation(AlexsCavesCacophony.MODID, "textures/entity/vallumraptor_painted.png"));
    		}
    	}
    }
}
