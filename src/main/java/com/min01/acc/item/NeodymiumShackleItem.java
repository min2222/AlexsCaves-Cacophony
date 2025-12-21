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
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand)
	{
		ItemStack stack = pPlayer.getItemInHand(pUsedHand);
        if(!pLevel.isClientSide) 
        {
        	stack.hurtAndBreak(1, pPlayer, (p_43388_) ->
        	{
        		p_43388_.broadcastBreakEvent(pUsedHand);
            });
        	EntityNeodymiumShackle shackle = new EntityNeodymiumShackle(pLevel, pPlayer, stack);
        	if(stack.getEnchantmentLevel(ACCEnchantments.ORBITING.get()) > 0)
        	{
        		shackle.setNoGravity(true);
        	}
        	shackle.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 1.0F);
        	shackle.setAnimationState(1);
        	shackle.setAnimationTick(10);
        	pLevel.addFreshEntity(shackle);
        	if(!pPlayer.getAbilities().instabuild) 
        	{
        		pPlayer.getInventory().removeItem(stack);
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
