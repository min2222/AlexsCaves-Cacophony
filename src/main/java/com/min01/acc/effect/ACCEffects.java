package com.min01.acc.effect;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.item.ACCItems;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ACCEffects
{
	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, AlexsCavesCacophony.MODID);
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, AlexsCavesCacophony.MODID);
	
	public static final RegistryObject<MobEffect> FEAR = EFFECTS.register("fear", () -> new FearEffect());
	
	public static final RegistryObject<Potion> FEAR_POTION = POTIONS.register("fear", () -> new Potion(new MobEffectInstance(FEAR.get(), 3600)));
	public static final RegistryObject<Potion> STRONG_FEAR_POTION = POTIONS.register("strong_fear", () -> new Potion(new MobEffectInstance(FEAR.get(), 1800, 1)));
	
	public static void init()
	{
		ItemStack water = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
		ItemStack fear = PotionUtils.setPotion(new ItemStack(Items.POTION), ACCEffects.FEAR_POTION.get());
		ItemStack strongFear = PotionUtils.setPotion(new ItemStack(Items.POTION), ACCEffects.STRONG_FEAR_POTION.get());
		BrewingRecipeRegistry.addRecipe(Ingredient.of(water), Ingredient.of(ACCItems.TREMORSAURUS_TOOTH.get()), fear);
		BrewingRecipeRegistry.addRecipe(Ingredient.of(fear), Ingredient.of(Items.GLOWSTONE_DUST), strongFear);
	}
}
