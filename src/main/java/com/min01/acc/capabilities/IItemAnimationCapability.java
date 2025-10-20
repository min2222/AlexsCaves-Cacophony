package com.min01.acc.capabilities;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.misc.SmoothAnimationState;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface IItemAnimationCapability extends INBTSerializable<CompoundTag>
{
	ResourceLocation ID = new ResourceLocation(AlexsCavesCacophony.MODID, "item_animation");

	void tick(Entity player, ItemStack stack);
	
	void setAnimationState(int state);
	
	int getAnimationState();
	
	SmoothAnimationState getAnimationStateByName(String name);
	
	void setAnimationTick(int tick);
	
	int getAnimationTick();
	
	int getTickCount();
}
