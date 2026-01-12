package com.min01.acc.network;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

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

	public static UpdateOwnerCapabilityPacket read(FriendlyByteBuf buf)
	{
		return new UpdateOwnerCapabilityPacket(buf.readUUID(), buf.readOptional(t -> t.readUUID()));
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeUUID(this.entityUUID);
		buf.writeOptional(this.ownerUUID, (t, u) -> t.writeUUID(u));
	}
	
	public static boolean handle(UpdateOwnerCapabilityPacket message, Supplier<NetworkEvent.Context> ctx) 
	{
		ctx.get().enqueueWork(() ->
		{
			if(ctx.get().getDirection().getReceptionSide().isClient())
			{
				ACCUtil.getClientLevel(level -> 
				{
					Entity entity = ACCUtil.getEntityByUUID(level, message.entityUUID);
					if(message.ownerUUID.isPresent())
					{
						Entity owner = ACCUtil.getEntityByUUID(level, message.ownerUUID.get());
						ACCUtil.setOwner(entity, owner);
					}
					else
					{
						ACCUtil.setOwner(entity, null);
					}
				});
			}
		});
		ctx.get().setPacketHandled(true);
		return true;
	}
}
