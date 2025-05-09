package com.min01.acc.animation;

import java.util.Optional;

import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;

public interface IHierarchicalPlayerModel<T extends LivingEntity>
{
	public ModelPart root();
	
	public Optional<ModelPart> getAnyDescendantWithName(String p_233394_);
	
	public void animate(T entity, String name, AnimationDefinition p_233383_, float p_233384_);

	public void animate(T entity, String name, AnimationDefinition p_233387_, float p_233388_, float p_233389_);
}
