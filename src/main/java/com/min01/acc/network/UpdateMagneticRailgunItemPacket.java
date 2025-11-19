package com.min01.acc.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.min01.acc.item.MagneticRailgunItem;
import com.min01.acc.util.ACCUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class UpdateMagneticRailgunItemPacket 
{
	public final ItemStack stack;
	public final UUID entityUUID;

	public UpdateMagneticRailgunItemPacket(ItemStack stack, UUID entityUUID) 
	{
		this.stack = stack;
		this.entityUUID = entityUUID;
	}

	public UpdateMagneticRailgunItemPacket(FriendlyByteBuf buf)
	{
		this.stack = buf.readItem();
		this.entityUUID = buf.readUUID();
	}

	public void encode(FriendlyByteBuf buf)
	{
		buf.writeItem(this.stack);
		buf.writeUUID(this.entityUUID);
	}

	public static class Handler 
	{
		public static boolean onMessage(UpdateMagneticRailgunItemPacket message, Supplier<NetworkEvent.Context> ctx)
		{
			ctx.get().enqueueWork(() ->
			{
				if(ctx.get().getDirection().getReceptionSide().isServer())
				{
					Entity entity = ACCUtil.getEntityByUUID(ctx.get().getSender().level, message.entityUUID);
					if(entity instanceof LivingEntity living) 
					{
		                ItemStack stackFrom = message.stack;
		                ItemStack to = null;
		                if(living.getItemInHand(InteractionHand.MAIN_HAND).is(stackFrom.getItem()))
		                {
		                    to = living.getItemInHand(InteractionHand.MAIN_HAND);
		                }
		                else if(living.getItemInHand(InteractionHand.OFF_HAND).is(stackFrom.getItem()))
		                {
		                    to = living.getItemInHand(InteractionHand.OFF_HAND);
		                }
		                if(to != null)
		                {
		                	if(to.getItem() instanceof MagneticRailgunItem item)
		                	{
		                		item.releaseUsingServer(to, living);
		                	}
		                }
					}
				}
			});
			ctx.get().setPacketHandled(true);
			return true;
		}
	}
}