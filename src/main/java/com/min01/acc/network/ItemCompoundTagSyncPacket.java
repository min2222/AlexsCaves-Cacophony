package com.min01.acc.network;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class ItemCompoundTagSyncPacket 
{
	private final ItemStack stack;
	private final CompoundTag tag;

	public ItemCompoundTagSyncPacket(ItemStack stack, CompoundTag tag) 
	{
		this.stack = stack;
		this.tag = tag;
	}

	public ItemCompoundTagSyncPacket(FriendlyByteBuf buf)
	{
		this.stack = buf.readItem();
		this.tag = buf.readNbt();
	}

	public void encode(FriendlyByteBuf buf)
	{
		buf.writeItem(this.stack);
		buf.writeNbt(this.tag);
	}

	public static class Handler 
	{
		public static boolean onMessage(ItemCompoundTagSyncPacket message, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() ->
			{
				message.stack.setTag(message.tag);
			});
			ctx.get().setPacketHandled(true);
			return true;
		}
	}
}
