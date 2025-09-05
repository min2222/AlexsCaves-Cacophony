package com.min01.acc.enchantment;

import com.github.alexmodguy.alexscaves.server.enchantment.ACWeaponEnchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ACCWeaponEnchantment extends ACWeaponEnchantment
{
	public ACCWeaponEnchantment(String name, Rarity rarity, EnchantmentCategory category, int levels, int minXP, EquipmentSlot... equipmentSlot) 
	{
		super(name, rarity, category, levels, minXP, equipmentSlot);
	}
}
