package com.min01.acc.network;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import com.min01.acc.capabilities.ACCCapabilities;
import com.min01.acc.util.ACCUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class UpdateOwnerCapabilityPacket 
{
	private final UUID entityUUID;
	private final Optional<UUID> ownerUUID;
	
	public UpdateOwnerCapabilityPacket(UUID entityUUID, Optional<UUID> ownerUUID) 
	{
		this.entityUUID = entityUUID;
		this.ownerUUID = ownerUUID;
	}

	public UpdateOwnerCapabilityPacket(FriendlyByteBuf buf)
	{
		this.entityUUID = buf.readUUID();
		this.ownerUUID = buf.readOptional(t -> t.readUUID());
	}

	public void encode(FriendlyByteBuf buf)
	{
		buf.writeUUID(this.entityUUID);
		buf.writeOptional(this.ownerUUID, (t, u) -> t.writeUUID(u));
	}
	
	public static class Handler 
	{
		public static boolean onMessage(UpdateOwnerCapabilityPacket message, Supplier<NetworkEvent.Context> ctx) 
		{
			ctx.get().enqueueWork(() ->
			{
				if(ctx.get().getDirection().getReceptionSide().isClient())
				{
					ACCUtil.getClientLevel(level -> 
					{
						Entity entity = ACCUtil.getEntityByUUID(level, message.entityUUID);
						entity.getCapability(ACCCapabilities.OWNER).ifPresent(cap -> 
						{
							if(message.ownerUUID.isPresent())
							{
								Entity owner = ACCUtil.getEntityByUUID(level, message.ownerUUID.get());
								cap.setOwner(owner);
							}
							else
							{
								cap.setOwner(null);
							}
						});
					});
				}
			});

			ctx.get().setPacketHandled(true);
			return true;
		}
	}
}
