package com.min01.acc.enchantment;

import com.github.alexmodguy.alexscaves.server.enchantment.ACEnchantmentRegistry;
import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ACCEnchantments
{
	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, AlexsCavesCacophony.MODID);
	
	public static final RegistryObject<Enchantment> RICOCHET = ENCHANTMENTS.register("ricochet", () -> new ACCWeaponEnchantment("ricochet", Enchantment.Rarity.COMMON, ACEnchantmentRegistry.RAYGUN, 1, 14, EquipmentSlot.MAINHAND));
	public static final RegistryObject<Enchantment> PULSE = ENCHANTMENTS.register("pulse", () -> new ACCWeaponEnchantment("pulse", Enchantment.Rarity.COMMON, ACEnchantmentRegistry.RAYGUN, 1, 14, EquipmentSlot.MAINHAND));
	public static final RegistryObject<Enchantment> OVERCHARGE = ENCHANTMENTS.register("overcharge", () -> new ACCWeaponEnchantment("overcharge", Enchantment.Rarity.VERY_RARE, ACEnchantmentRegistry.RAYGUN, 1, 12, EquipmentSlot.MAINHAND));
	
	public static boolean areCompatible(ACCWeaponEnchantment enchantment1, Enchantment enchantment2)
	{
		if(enchantment1 == PULSE.get() && (enchantment2 == RICOCHET.get() || enchantment2 == ACEnchantmentRegistry.ENERGY_EFFICIENCY.get()))
		{
			return false;
		}
		if(enchantment1 == OVERCHARGE.get())
		{
			return false;
		}
		return true;
	}
	
	public static void addAllEnchantsToCreativeTab(BuildCreativeModeTabContentsEvent event, EnchantmentCategory enchantmentCategory)
	{
		for(RegistryObject<Enchantment> enchantObject : ENCHANTMENTS.getEntries())
		{
			if(enchantObject.isPresent())
			{
				Enchantment enchant = enchantObject.get();
				if(enchant.category == enchantmentCategory)
				{
					EnchantmentInstance instance = new EnchantmentInstance(enchant, enchant.getMaxLevel());
					event.accept(EnchantedBookItem.createForEnchantment(instance));
				}
			}
		}
	}
}
