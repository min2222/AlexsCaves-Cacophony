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
	static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, "shoot_nuclear_bomb");

	@Override
	public ResourceLocation getId() 
	{
		return ID;
	}

	@Override
	protected TriggerInstance createInstance(JsonObject pJson, ContextAwarePredicate pPredicate, DeserializationContext pDeserializationContext)
	{
		return new ShootNuclearBombTrigger.TriggerInstance(pPredicate);
	}
	
	public void trigger(ServerPlayer pPlayer)
	{
		this.trigger(pPlayer, t -> true);
	}
	
	public static class TriggerInstance extends AbstractCriterionTriggerInstance
	{
		public TriggerInstance(ContextAwarePredicate pPlayer) 
		{
			super(ID, pPlayer);
		}
	}
}