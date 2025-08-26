package com.min01.acc.event;

import com.github.alexmodguy.alexscaves.server.misc.ACCreativeTabRegistry;
import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.effect.ACCEffects;
import com.min01.acc.entity.ACCEntities;
import com.min01.acc.entity.living.EntityGloomworm;
import com.min01.acc.entity.living.EntityOvivenator;
import com.min01.acc.item.ACCItems;

import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = AlexsCavesCacophony.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EventHandler
{
	@SubscribeEvent
	public static void onFMLCommonSetup(FMLCommonSetupEvent event)
	{
		ItemStack water = PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.WATER);
		ItemStack fear = PotionUtils.setPotion(new ItemStack(Items.POTION), ACCEffects.FEAR_POTION.get());
		ItemStack strongFear = PotionUtils.setPotion(new ItemStack(Items.POTION), ACCEffects.STRONG_FEAR_POTION.get());
		BrewingRecipeRegistry.addRecipe(Ingredient.of(water), Ingredient.of(ACCItems.TREMORSAURUS_TOOTH.get()), fear);
		BrewingRecipeRegistry.addRecipe(Ingredient.of(fear), Ingredient.of(Items.GLOWSTONE_DUST), strongFear);
	}
	
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) 
    {
    	event.put(ACCEntities.GLOOMWORM.get(), EntityGloomworm.createAttributes().build());
    	event.put(ACCEntities.OVIVENATOR.get(), EntityOvivenator.createAttributes().build());
    }
    
    @SubscribeEvent
    public static void onSpawnPlacementRegister(SpawnPlacementRegisterEvent event)
    {
    	event.register(ACCEntities.OVIVENATOR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EntityOvivenator::checkPrehistoricSpawnRules, SpawnPlacementRegisterEvent.Operation.AND);
    }
    
    @SubscribeEvent
    public static void onBuildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event)
    {
    	if(event.getTabKey() == ACCreativeTabRegistry.TOXIC_CAVES.getKey())
    	{
    		event.accept(ACCItems.RAYBLAE.get());
    		event.accept(ACCItems.RAY_CATALYST.get());
    		event.accept(ACCItems.RADRIFLE.get());
    	}
    	if(event.getTabKey() == ACCreativeTabRegistry.ABYSSAL_CHASM.getKey())
    	{
    		event.accept(ACCItems.HULLBREAKER_TOOTH.get());
    		event.accept(ACCItems.MASTCARVER.get());
    	}
    	if(event.getTabKey() == ACCreativeTabRegistry.PRIMORDIAL_CAVES.getKey())
    	{
    		event.accept(ACCItems.RAW_DINO_DRUMSTICK.get());
    		event.accept(ACCItems.COOKED_DINO_DRUMSTICK.get());
    		event.accept(ACCItems.TREMORSAURUS_TOOTH.get());
    		event.accept(ACCItems.ARROW_OF_FEAR.get());
    		event.accept(ACCItems.OVIVENATOR_SPAWN_EGG.get());
    		event.accept(ACCItems.OVIVENATOR_EGG.get());
    	}
    	if(event.getTabKey() == ACCreativeTabRegistry.FORLORN_HOLLOWS.getKey())
    	{
    		event.accept(ACCItems.GLOOMOTH_COCOON.get());
    		event.accept(ACCItems.GLOOMOTH_EGGS.get());
    		event.accept(ACCItems.GLOOMWORM_SPAWN_EGG.get());
    	}
    	if(event.getTabKey() == ACCreativeTabRegistry.MAGNETIC_CAVES.getKey())
    	{
    		event.accept(ACCItems.NEODYMIUM_SHACKLE.get());
    		event.accept(ACCItems.MAGNETIC_RAILGUN.get());
    	}
    }
}