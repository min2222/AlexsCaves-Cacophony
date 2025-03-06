package com.min01.acc.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.min01.acc.capabilities.ACCCapabilities;
import com.min01.acc.capabilities.IPlayerAnimationCapability;
import com.min01.acc.capabilities.PlayerAnimationCapabilityImpl;
import com.min01.acc.util.ACCUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

public class UpdatePlayerAnimationTickPacket 
{
	public final UUID uuid;
	public final IPlayerAnimationCapability cap;

	public UpdatePlayerAnimationTickPacket(UUID uuid, IPlayerAnimationCapability cap) 
	{
		this.uuid = uuid;
		this.cap = cap;
	}

	public UpdatePlayerAnimationTickPacket(FriendlyByteBuf buf)
	{
		this.uuid = buf.readUUID();
		IPlayerAnimationCapability cap = new PlayerAnimationCapabilityImpl();
		cap.deserializeNBT(buf.readNbt());
		this.cap = cap;
	}

	public void encode(FriendlyByteBuf buf)
	{
		buf.writeUUID(this.uuid);
		buf.writeNbt(this.cap.serializeNBT());
	}

	public static class Handler 
	{
		public static boolean onMessage(UpdatePlayerAnimationTickPacket message, Supplier<NetworkEvent.Context> ctx)
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
							living.getCapability(ACCCapabilities.PLAYER_ANIMATION).ifPresent(t -> 
							{
								t.setAnimationTick(message.cap.getAnimationTick());
							});
						}
					});
				}
			});
			ctx.get().setPacketHandled(true);
			return true;
		}
	}
}
