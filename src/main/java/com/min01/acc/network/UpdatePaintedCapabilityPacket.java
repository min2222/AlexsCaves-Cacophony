package com.min01.acc.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.min01.acc.capabilities.ACCCapabilities;
import com.min01.acc.util.ACCUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class UpdatePaintedCapabilityPacket 
{
	private final UUID entityUUID;
	private final boolean isPainted;
	
	public UpdatePaintedCapabilityPacket(UUID entityUUID, boolean isPainted) 
	{
		this.entityUUID = entityUUID;
		this.isPainted = isPainted;
	}

	public UpdatePaintedCapabilityPacket(FriendlyByteBuf buf)
	{
		this.entityUUID = buf.readUUID();
		this.isPainted = buf.readBoolean();
	}

	public void encode(FriendlyByteBuf buf)
	{
		buf.writeUUID(this.entityUUID);
		buf.writeBoolean(this.isPainted);
	}
	
	public static class Handler 
	{
		public static boolean onMessage(UpdatePaintedCapabilityPacket message, Supplier<NetworkEvent.Context> ctx) 
		{
			ctx.get().enqueueWork(() ->
			{
				if(ctx.get().getDirection().getReceptionSide().isClient())
				{
					ACCUtil.getClientLevel(level -> 
					{
						Entity entity = ACCUtil.getEntityByUUID(level, message.entityUUID);
						entity.getCapability(ACCCapabilities.PAINTED).ifPresent(cap -> 
						{
							cap.setPainted(message.isPainted);
						});
					});
				}
			});

			ctx.get().setPacketHandled(true);
			return true;
		}
	}
}
