package com.min01.acc.advancements.critereon;

import com.google.gson.JsonObject;
import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ShootNuclearBombTrigger extends SimpleCriterionTrigger<ShootNuclearBombTrigger.TriggerInstance> 
{
	static final ResourceLocation ID = new ResourceLocation(AlexsCavesCacophony.MODID, "shoot_nuclear_bomb");

	@Override
	public ResourceLocation getId() 
	{
		return ID;
	}

	@Override
	protected TriggerInstance createInstance(JsonObject p_66248_, ContextAwarePredicate p_286603_, DeserializationContext p_66250_)
	{
		return new ShootNuclearBombTrigger.TriggerInstance(p_286603_);
	}
	
	public void trigger(ServerPlayer p_46872_)
	{
		this.trigger(p_46872_, t -> true);
	}
	
	public static class TriggerInstance extends AbstractCriterionTriggerInstance
	{
		public TriggerInstance(ContextAwarePredicate p_286466_) 
		{
			super(ID, p_286466_);
		}
	}
}