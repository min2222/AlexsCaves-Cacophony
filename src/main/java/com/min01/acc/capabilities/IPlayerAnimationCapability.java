package com.min01.acc.capabilities;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.misc.SmoothAnimationState;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface IPlayerAnimationCapability extends INBTSerializable<CompoundTag>
{
	ResourceLocation ID = new ResourceLocation(AlexsCavesCacophony.MODID, "player_animation");

	void setEntity(Player entity);
	
	void tick();
	
	void setAnimationState(int state);
	
	int getAnimationState();
	
	SmoothAnimationState getAnimationStateByName(String name);
	
	void setAnimationTick(int tick);
	
	int getAnimationTick();
}
