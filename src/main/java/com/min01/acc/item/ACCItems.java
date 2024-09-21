package com.min01.acc.item;

import java.util.function.Supplier;

import com.min01.acc.AlexsCavesCacophony;
import com.min01.acc.block.ACCBlocks;
import com.min01.acc.entity.ACCEntities;
import com.min01.acc.misc.ACCTier;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ACCItems 
{
	public static final ACCTier TIER_MASTCARVER = new ACCTier(3, 1000, 1.0F, 1.0F, 15, () -> Ingredient.EMPTY);
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AlexsCavesCacophony.MODID);

	public static final RegistryObject<Item> GLOOMWORM_SPAWN_EGG = registerSpawnEgg("gloomworm_spawn_egg", () -> ACCEntities.GLOOMWORM.get(), 15455166, 10708554);
	
	public static final RegistryObject<Item> RAYBLAE = ITEMS.register("rayblade", () -> new RaybladeItem());
	public static final RegistryObject<Item> RAY_CATALYST = ITEMS.register("ray_catalyst", () -> new RayCatalystItem());
	public static final RegistryObject<Item> RADRIFLE = ITEMS.register("radrifle", () -> new RadrifleItem());
	
	public static final RegistryObject<Item> HULLBREAKER_TOOTH = ITEMS.register("hullbreaker_tooth", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> MASTCARVER = ITEMS.register("mastcarver", () -> new MastcarverItem());
	
	public static final RegistryObject<Item> RAW_DINO_DRUMSTICK = ITEMS.register("raw_dino_drumstick", () -> new DinoDrumstickItem(new FoodProperties.Builder().saturationMod(2.5F).nutrition(10).build(), true));
	public static final RegistryObject<Item> COOKED_DINO_DRUMSTICK = ITEMS.register("cooked_dino_drumstick", () -> new DinoDrumstickItem(new FoodProperties.Builder().saturationMod(5.0F).nutrition(20).build(), false));
	public static final RegistryObject<Item> TREMORSAURUS_TOOTH = ITEMS.register("tremorsaurus_tooth", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> ARROW_OF_FEAR = ITEMS.register("arrow_of_fear", () -> new FearArrowItem());
	
	public static final RegistryObject<Item> GLOOMOTH_COCOON = registerBlockItem("gloomoth_cocoon", () -> ACCBlocks.GLOOMOTH_COCOON.get(), new Item.Properties());
	
	public static RegistryObject<Item> registerBlockItem(String name, Supplier<Block> block, Item.Properties propertie)
	{
		return ITEMS.register(name, () -> new BlockItem(block.get(), propertie));
	}
	
	public static <T extends Mob> RegistryObject<Item> registerSpawnEgg(String name, Supplier<EntityType<T>> entity, int color1, int color2) 
	{
		return ITEMS.register(name, () -> new ForgeSpawnEggItem(entity, color1, color2, new Item.Properties()));
	}
}
