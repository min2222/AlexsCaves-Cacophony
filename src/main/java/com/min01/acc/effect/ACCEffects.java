package com.min01.acc.effect;

import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ACCEffects
{
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, AlexsCavesCacophony.MODID);
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, AlexsCavesCacophony.MODID);
	
	public static final RegistryObject<MobEffect> FEAR = EFFECTS.register("fear", () -> new FearEffect());
	
	public static final RegistryObject<Potion> FEAR_POTION = POTIONS.register("fear", () -> new Potion(new MobEffectInstance(FEAR.get(), 1)));
}
