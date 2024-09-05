package com.min01.acc.item;

import com.min01.acc.entity.projectile.EntityFearArrow;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FearArrowItem extends ArrowItem 
{
    public FearArrowItem() 
    {
        super(new Item.Properties());
    }

    @Override
    public AbstractArrow createArrow(Level level, ItemStack itemStack, LivingEntity livingEntity)
    {
        return new EntityFearArrow(level, livingEntity);
    }
}
