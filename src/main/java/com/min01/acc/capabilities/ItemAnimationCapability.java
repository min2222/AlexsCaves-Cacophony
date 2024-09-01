package com.min01.acc.capabilities;

import java.util.Map;

import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface ItemAnimationCapability extends INBTSerializable<CompoundTag>
{
	ResourceLocation ID = new ResourceLocation(AlexsCavesCacophony.MODID, "item_animation");

	void setEntity(LivingEntity entity);
	
	void setItemStack(ItemStack stack);
	
	void stopAnimation(String name);
	
	void playAnimation(String name, int tickCount);
	
	void addAnimationState(String name);
	
	AnimationState getAnimationState(String name);

	Map<String, AnimationState> getAnimMap();
}
