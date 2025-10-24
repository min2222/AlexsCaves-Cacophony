package com.min01.acc.item;

import com.min01.acc.enchantment.ACCEnchantments;
import com.min01.acc.entity.projectile.EntityNeodymiumShackle;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;

public class NeodymiumShackleItem extends Item
{
	public NeodymiumShackleItem() 
	{
		super(new Item.Properties().durability(250).fireResistant().rarity(Rarity.UNCOMMON));
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_)
	{
		ItemStack stack = p_41433_.getItemInHand(p_41434_);
        if(!p_41432_.isClientSide) 
        {
        	stack.hurtAndBreak(1, p_41433_, (p_43388_) ->
        	{
        		p_43388_.broadcastBreakEvent(p_41434_);
            });
        	EntityNeodymiumShackle shackle = new EntityNeodymiumShackle(p_41432_, p_41433_, stack);
        	if(stack.getEnchantmentLevel(ACCEnchantments.ORBITING.get()) > 0)
        	{
        		shackle.setNoGravity(true);
        	}
        	shackle.shootFromRotation(p_41433_, p_41433_.getXRot(), p_41433_.getYRot(), 0.0F, 1.5F, 1.0F);
        	shackle.setAnimationState(1);
        	shackle.setAnimationTick(10);
        	p_41432_.addFreshEntity(shackle);
        	if(!p_41433_.getAbilities().instabuild) 
        	{
        		p_41433_.getInventory().removeItem(stack);
        	}
        	else
        	{
        		shackle.pickup = AbstractArrow.Pickup.DISALLOWED;
        	}
        }
		return InteractionResultHolder.consume(stack);
	}
	
    @Override
    public int getEnchantmentValue()
    {
        return 1;
    }
}
