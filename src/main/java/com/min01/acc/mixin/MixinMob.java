package com.min01.acc.mixin;

import javax.annotation.Nullable;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.min01.acc.entity.IPainted;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;

@Mixin(Mob.class)
public class MixinMob 
{
	@Inject(at = @At(value = "HEAD"), method = "finalizeSpawn")
	private void finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, @Nullable SpawnGroupData p_21437_, @Nullable CompoundTag p_21438_, CallbackInfoReturnable<SpawnGroupData> cir)
	{
		Mob entity = Mob.class.cast(this);
		if(entity instanceof IPainted painted)
		{
			painted.setPainted(Math.random() <= 0.1F);
		}
	}
}
