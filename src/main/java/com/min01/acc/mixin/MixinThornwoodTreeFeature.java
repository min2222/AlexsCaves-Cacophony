package com.min01.acc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.github.alexmodguy.alexscaves.server.level.feature.ThornwoodTreeFeature;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.min01.acc.block.ACCBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(ThornwoodTreeFeature.class)
public class MixinThornwoodTreeFeature 
{
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/WorldGenLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z", ordinal = 3), method = "place")
	private boolean setBlock(WorldGenLevel instance, BlockPos blockPos, BlockState blockState, int i)
	{
		for(Direction direction : ACMath.HORIZONTAL_DIRECTIONS)
		{
			BlockPos pos = blockPos.relative(direction);
			if(instance.getBlockState(pos).isAir() && instance.getRandom().nextFloat() <= 0.1F)
			{
				instance.setBlock(pos, ACCBlocks.GLOOMOTH_COCOON.get().defaultBlockState(), i);
				break;
			}
		}
		return instance.setBlock(blockPos, blockState, i);
	}
}
