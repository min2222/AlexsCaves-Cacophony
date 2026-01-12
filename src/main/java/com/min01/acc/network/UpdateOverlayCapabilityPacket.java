package com.min01.acc.network;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import com.min01.acc.util.ACCUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class UpdateOverlayCapabilityPacket 
{
	private final UUID entityUUID;
	private final Map<String, Integer> progressMap;
	
	public UpdateOverlayCapabilityPacket(UUID entityUUID, Map<String, Integer> progressMap) 
	{
		this.entityUUID = entityUUID;
		this.progressMap = progressMap;
	}

	public static UpdateOverlayCapabilityPacket read(FriendlyByteBuf buf)
	{
		return new UpdateOverlayCapabilityPacket(buf.readUUID(), buf.readMap(t -> t.readUtf(), t -> t.readInt()));
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeUUID(this.entityUUID);
		buf.writeMap(this.progressMap, (t, u) -> t.writeUtf(u), (t, u) -> t.writeInt(u));
	}
	
	public static boolean handle(UpdateOverlayCapabilityPacket message, Supplier<NetworkEvent.Context> ctx) 
	{
		ctx.get().enqueueWork(() ->
		{
			if(ctx.get().getDirection().getReceptionSide().isClient())
			{
				ACCUtil.getClientLevel(level -> 
				{
					Entity entity = ACCUtil.getEntityByUUID(level, message.entityUUID);
					for(Map.Entry<String, Integer> entry : message.progressMap.entrySet())
					{
						ACCUtil.setOverlayProgress(entry.getKey(), entry.getValue(), entity);
					}
				});
			}
		});
		ctx.get().setPacketHandled(true);
		return true;
	}
}
