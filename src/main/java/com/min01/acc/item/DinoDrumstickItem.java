package com.min01.acc.item;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.github.alexmodguy.alexscaves.server.message.UpdateEffectVisualityEntityMessage;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DinoDrumstickItem extends Item
{
	public boolean isRaw;
	public DinoDrumstickItem(FoodProperties properties, boolean isRaw) 
	{
		super(new Item.Properties().food(properties));
		this.isRaw = isRaw;
	}
	
	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity victim, LivingEntity attacker) 
	{
		if(!victim.level.isClientSide)
		{
			if(victim.getRandom().nextFloat() < 0.8F)
			{
				MobEffectInstance instance = new MobEffectInstance(ACEffectRegistry.STUNNED.get(), victim.getRandom().nextInt(1, this.isRaw ? 4 : 6) * 20, 0);
				if(victim.addEffect(instance))
				{
	                AlexsCaves.sendMSGToAll(new UpdateEffectVisualityEntityMessage(victim.getId(), attacker.getId(), 3, instance.getDuration()));
				}
			}
		}
		return super.hurtEnemy(stack, victim, attacker);
	}
	
	@Override
	public ItemStack finishUsingItem(ItemStack p_43263_, Level p_43264_, LivingEntity p_43265_)
	{
		ItemStack stack = super.finishUsingItem(p_43263_, p_43264_, p_43265_);
		if(stack.getCount() > 1)
		{
			if(p_43265_ instanceof Player player && !player.getAbilities().instabuild)
			{
				stack.shrink(1);
				player.getInventory().add(new ItemStack(ACItemRegistry.HEAVY_BONE.get()));
			}
			return stack;
		}
		return p_43265_ instanceof Player player && player.getAbilities().instabuild ? stack : new ItemStack(ACItemRegistry.HEAVY_BONE.get());
	}
	
    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) 
    {
        return player.getAttackStrengthScale(0) < 0.95 || player.attackAnim != 0;
    }
    
    @Override
    public int getUseDuration(ItemStack p_41454_)
    {
    	return 64;
    }
	
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) 
    {
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder2 = ImmutableMultimap.builder();
		builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 2.0D, AttributeModifier.Operation.ADDITION));
		builder2.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Tool modifier", 3.0D, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -3.75F, AttributeModifier.Operation.ADDITION));
		builder2.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Tool modifier", -3.75F, AttributeModifier.Operation.ADDITION));
		if(slot == EquipmentSlot.MAINHAND)
		{
	    	return this.isRaw ? builder.build() : builder2.build();
		}
    	return super.getAttributeModifiers(slot, stack);
    }
}
