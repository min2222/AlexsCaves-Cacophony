package com.min01.acc.capabilities;

import com.min01.acc.item.animation.IAnimatableItem;
import com.min01.acc.misc.ACCTags;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;

public class ACCCapabilities
{
	public static void onAttachItemStackCapabilities(AttachCapabilitiesEvent<ItemStack> event)
	{
    	ItemStack stack = event.getObject();
    	if(stack.getItem() instanceof IAnimatableItem)
    	{
    		event.addCapability(ItemAnimationCapabilityImpl.ID, new ItemAnimationCapabilityImpl(stack));
    	}
	}
	
	public static void onAttachEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
	{
    	Entity entity = event.getObject();
		event.addCapability(OwnerCapabilityImpl.ID, new OwnerCapabilityImpl(entity));
		if(entity instanceof Player player)
		{
			event.addCapability(PlayerAnimationCapabilityImpl.ID, new PlayerAnimationCapabilityImpl(player));
			event.addCapability(OverlayCapabilityImpl.ID, new OverlayCapabilityImpl(player));
		}
    	if(entity.getType().is(ACCTags.ACCEntity.PAINTABLE))
    	{
    		event.addCapability(PaintedCapabilityImpl.ID, new PaintedCapabilityImpl(entity));
    	}
	}
}
