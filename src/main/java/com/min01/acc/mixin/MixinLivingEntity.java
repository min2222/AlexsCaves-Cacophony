package com.min01.acc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.min01.acc.entity.IPainted;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class MixinLivingEntity
{
	@Inject(at = @At(value = "HEAD"), method = "addAdditionalSaveData")
	private void addAdditionalSaveData(CompoundTag tag, CallbackInfo ci)
	{
		LivingEntity living = LivingEntity.class.cast(this);
		if(living instanceof IPainted painted)
		{
			tag.putBoolean("isPainted", painted.isPainted());
		}
	}
	
	@Inject(at = @At(value = "HEAD"), method = "readAdditionalSaveData")
	private void readAdditionalSaveData(CompoundTag tag, CallbackInfo ci)
	{
		LivingEntity living = LivingEntity.class.cast(this);
		if(living instanceof IPainted painted)
		{
			if(tag.contains("isPainted"))
			{
				painted.setPainted(tag.getBoolean("isPainted"));
			}
		}
	}
}
