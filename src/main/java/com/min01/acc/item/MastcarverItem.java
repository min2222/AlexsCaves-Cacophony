package com.min01.acc.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.min01.acc.util.ACCUtil;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class MastcarverItem extends SwordItem
{
    public static final String STACKED_DMG = "StackedDmg";
    public static final String LAST_HIT_ENTITY = "LastHitEntity";
    public static final int MAX_STACK = 100;
    
	public MastcarverItem() 
	{
		super(ACCItems.TIER_MASTCARVER, 0, 0, new Item.Properties().durability(1000).rarity(Rarity.EPIC));
	}
	
	@Override
	public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_)
	{
		if(getStackedDmg(p_41404_) <= 0.0F)
		{
			setStackedDmg(p_41404_, 6.0F);
		}
	}
	
	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity victim, LivingEntity attacker) 
	{
		float stackedDmg = getStackedDmg(stack);
		Entity entity = getLastHitEntity(stack, attacker.level);
		if(entity != null)
		{
			if(entity == victim)
			{
				setStackedDmg(stack, Math.min(stackedDmg + 1, MAX_STACK));
			}
			else
			{
				setStackedDmg(stack, 6.0F);
				setLastHitEntity(stack, victim);
			}
		}
		else
		{
			if(!stack.getTag().contains(LAST_HIT_ENTITY))
			{
				setStackedDmg(stack, Math.min(stackedDmg + 1, MAX_STACK));
			}
			else
			{
				setStackedDmg(stack, 6.0F);
			}
			setLastHitEntity(stack, victim);
		}
		return super.hurtEnemy(stack, victim, attacker);
	}
	
	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction) 
	{
		return toolAction == ToolActions.SWORD_DIG;
	}
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) 
	{
		return newStack.getItem() != this;
	}
	
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) 
    {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", getStackedDmg(stack), AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -3.0D, AttributeModifier.Operation.ADDITION));
    	return slot == EquipmentSlot.MAINHAND ? builder.build() : super.getAttributeModifiers(slot, stack);
    }
    
    public static Entity getLastHitEntity(ItemStack stack, Level level)
    {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(LAST_HIT_ENTITY) ? ACCUtil.getEntityByUUID(level, tag.getUUID(LAST_HIT_ENTITY)) : null;
    }

    public static void setLastHitEntity(ItemStack stack, Entity entity)
    {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putUUID(LAST_HIT_ENTITY, entity.getUUID());
    }
    
    public static float getStackedDmg(ItemStack stack)
    {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(STACKED_DMG) ? tag.getFloat(STACKED_DMG) : 6.0F;
    }

    public static void setStackedDmg(ItemStack stack, float dmg)
    {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putFloat(STACKED_DMG, dmg);
    }
}
