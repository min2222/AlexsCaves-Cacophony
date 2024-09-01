package com.min01.acc.item;

import java.util.function.Consumer;

import com.github.alexmodguy.alexscaves.server.item.ACItemRegistry;
import com.min01.acc.capabilities.ACCCapabilities;
import com.min01.acc.item.renderer.RaybladeRenderer;
import com.min01.acc.util.ACCClientUtil;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class RaybladeItem extends SwordItem
{
    public static final String FRAME = "Frame";
    public static final String ACTIVATED = "Activate";
    
	public RaybladeItem()
	{
		super(Tiers.NETHERITE, 0, 0, new Item.Properties().durability(3).rarity(ACItemRegistry.RARITY_NUCLEAR));
	}
	
	@Override
	public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_)
	{
		CompoundTag tag = p_41404_.getOrCreateTag();
		if(tag.contains(FRAME))
		{
			if(p_41406_.tickCount % 2 == 0)
			{
				tag.putInt(FRAME, tag.getInt(FRAME) + 1);
			}
			
			if(tag.getInt(FRAME) > 19)
			{
				tag.putInt(FRAME, 0);
			}
		}
		else
		{
			tag.putInt(FRAME, 0);
		}
	}
	
	public static int getFrame(ItemStack stack)
	{
		return stack.getOrCreateTag().getInt(FRAME);
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, InteractionHand p_41434_) 
	{
		ItemStack stack = p_41433_.getItemInHand(p_41434_);
		if(p_41433_.isShiftKeyDown())
		{
			stack.getCapability(ACCCapabilities.ITEM_ANIMATION).ifPresent(cap -> cap.stopAnimation(ACTIVATED));
		}
		else
		{
			stack.getCapability(ACCCapabilities.ITEM_ANIMATION).ifPresent(cap -> cap.playAnimation(ACTIVATED, p_41433_.tickCount));
		}
		return super.use(p_41432_, p_41433_, p_41434_);
	}
	
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) 
	{
		consumer.accept(new IClientItemExtensions()
		{
			@Override
			public BlockEntityWithoutLevelRenderer getCustomRenderer() 
			{
				return new RaybladeRenderer(ACCClientUtil.MC.getEntityModels());
			}
		});
	}
}
