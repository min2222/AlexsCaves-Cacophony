package com.min01.acc.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.min01.acc.capabilities.ACCCapabilities;
import com.min01.acc.util.ACCUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class UpdatePlayerAnimationTickPacket 
{
	public final UUID uuid;
	public final int animationTick;

	public UpdatePlayerAnimationTickPacket(UUID uuid, int animationTick) 
	{
		this.uuid = uuid;
		this.animationTick = animationTick;
	}

	public UpdatePlayerAnimationTickPacket(FriendlyByteBuf buf)
	{
		this.uuid = buf.readUUID();
		this.animationTick = buf.readInt();
	}

	public void encode(FriendlyByteBuf buf)
	{
		buf.writeUUID(this.uuid);
		buf.writeInt(this.animationTick);
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
						if(entity instanceof Player player)
						{
							player.getCapability(ACCCapabilities.PLAYER_ANIMATION).ifPresent(t -> 
							{
								t.setAnimationTick(message.animationTick);
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