package com.min01.acc.network;

import java.util.function.Supplier;

import com.min01.acc.capabilities.ACCCapabilities;
import com.min01.acc.util.ACCClientUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class ItemAnimationPlayPacket 
{
	private final int entityId;
	private final ItemStack stack;
	private final String name;
	
	public ItemAnimationPlayPacket(Entity entity, ItemStack stack, String name) 
	{
		this.entityId = entity.getId();
		this.stack = stack;
		this.name = name;
	}

	public ItemAnimationPlayPacket(FriendlyByteBuf buf)
	{
		this.entityId = buf.readInt();
		this.stack = buf.readItem();
		this.name = buf.readUtf();
	}

	public void encode(FriendlyByteBuf buf)
	{
		buf.writeInt(this.entityId);
		buf.writeItem(this.stack);
		buf.writeUtf(this.name);
	}
	
	public static class Handler 
	{
		public static boolean onMessage(ItemAnimationPlayPacket message, Supplier<NetworkEvent.Context> ctx) 
		{
			ctx.get().enqueueWork(() ->
			{
				Entity entity = ACCClientUtil.MC.level.getEntity(message.entityId);
				if(entity instanceof LivingEntity living)
				{
					for(InteractionHand hands : InteractionHand.values())
					{
						ItemStack stack = living.getItemInHand(hands);
						stack.getCapability(ACCCapabilities.ITEM_ANIMATION).ifPresent(cap -> 
						{
							cap.getAnimMap().get(message.name).startIfStopped(living.tickCount);
						});
					}
				}
			});

			ctx.get().setPacketHandled(true);
			return true;
		}
	}
}
