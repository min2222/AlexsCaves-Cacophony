package com.min01.acc.enchantment;

import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ACCEnchantments
{
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, AlexsCavesCacophony.MODID);
	
	public static final RegistryObject<Enchantment> RECOCHET = ENCHANTMENTS.register("recochet", () -> new ACCWeaponEnchantment("recochet", Enchantment.Rarity.COMMON, ACEnchantmentRegistry.RAYGUN, 1, 14, EquipmentSlot.MAINHAND));
	public static final RegistryObject<Enchantment> PULSE = ENCHANTMENTS.register("pulse", () -> new ACCWeaponEnchantment("pulse", Enchantment.Rarity.COMMON, ACEnchantmentRegistry.RAYGUN, 1, 14, EquipmentSlot.MAINHAND));
	public static final RegistryObject<Enchantment> OVERCHARGE = ENCHANTMENTS.register("overcharge", () -> new ACCWeaponEnchantment("overcharge", Enchantment.Rarity.VERY_RARE, ACEnchantmentRegistry.RAYGUN, 1, 12, EquipmentSlot.MAINHAND));
}
