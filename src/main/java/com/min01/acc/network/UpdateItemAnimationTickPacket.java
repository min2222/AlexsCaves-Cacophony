package com.min01.acc.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.min01.acc.capabilities.ACCCapabilities;
import com.min01.acc.util.ACCUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class UpdateItemAnimationTickPacket 
{
	public final UUID uuid;
	public final ItemStack stack;
	public final int animationTick;

	public UpdateItemAnimationTickPacket(UUID uuid, ItemStack stack, int animationTick) 
	{
		this.uuid = uuid;
		this.stack = stack;
		this.animationTick = animationTick;
	}

	public UpdateItemAnimationTickPacket(FriendlyByteBuf buf)
	{
		this.uuid = buf.readUUID();
		this.stack = buf.readItem();
		this.animationTick = buf.readInt();
	}

	public void encode(FriendlyByteBuf buf)
	{
		buf.writeUUID(this.uuid);
		buf.writeItem(this.stack);
		buf.writeInt(this.animationTick);
	}

	public static class Handler 
	{
		public static boolean onMessage(UpdateItemAnimationTickPacket message, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() ->
			{
				if(ctx.get().getDirection().getReceptionSide().isClient()) 
				{
					ACCUtil.getClientLevel(level -> 
					{
						Entity entity = ACCUtil.getEntityByUUID(level, message.uuid);
						if(entity instanceof LivingEntity living)
						{
			                ItemStack stack = message.stack;
			                ItemStack to = null;
			                if(living.getItemInHand(InteractionHand.MAIN_HAND).is(stack.getItem()))
			                {
			                    to = living.getItemInHand(InteractionHand.MAIN_HAND);
			                }
			                else if(living.getItemInHand(InteractionHand.OFF_HAND).is(stack.getItem()))
			                {
			                    to = living.getItemInHand(InteractionHand.OFF_HAND);
			                }
			                if(to != null)
			                {
			                	to.getCapability(ACCCapabilities.ITEM_ANIMATION).ifPresent(t -> 
			                	{
			                		t.setAnimationTick(message.animationTick);
			                	});
			                }
						}
					});
				}
			});
			ctx.get().setPacketHandled(true);
			return true;
		}
	}
}