package com.min01.acc.capabilities;

import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface IPlayerAnimationCapability extends INBTSerializable<CompoundTag>
{
	ResourceLocation ID = new ResourceLocation(AlexsCavesCacophony.MODID, "player_animation");

	void setEntity(Player entity);
	
	void startPlayerAnimation(String name);
	
	void stopPlayerAnimation(String name);
	
	AnimationState getAnimationState(String name);
	
	void setAnimationTick(int tick);
	
	int getAnimationTick();
	
	CompoundTag getCompoundTag();
}
