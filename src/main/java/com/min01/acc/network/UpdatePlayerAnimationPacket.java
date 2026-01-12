package com.min01.acc.network;

import java.util.UUID;
import java.util.function.Supplier;

import com.min01.acc.capabilities.PlayerAnimationCapabilityImpl;
import com.min01.acc.util.ACCUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

public class UpdatePlayerAnimationPacket 
{
	public final UUID uuid;
	public final int animationState;
	public final int prevAimationState;
	public final int animationTick;

	public UpdatePlayerAnimationPacket(UUID uuid, int animationState, int prevAimationState, int animationTick) 
	{
		this.uuid = uuid;
		this.animationState = animationState;
		this.prevAimationState = prevAimationState;
		this.animationTick = animationTick;
	}

	public static UpdatePlayerAnimationPacket read(FriendlyByteBuf buf)
	{
		return new UpdatePlayerAnimationPacket(buf.readUUID(), buf.readInt(), buf.readInt(), buf.readInt());
	}

	public void write(FriendlyByteBuf buf)
	{
		buf.writeUUID(this.uuid);
		buf.writeInt(this.animationState);
		buf.writeInt(this.prevAimationState);
		buf.writeInt(this.animationTick);
	}

	public static boolean handle(UpdatePlayerAnimationPacket message, Supplier<NetworkEvent.Context> ctx)
	{
		ctx.get().enqueueWork(() ->
		{
			if(ctx.get().getDirection().getReceptionSide().isClient()) 
			{
				ACCUtil.getClientLevel(level -> 
				{
					Entity entity = ACCUtil.getEntityByUUID(level, message.uuid);
					entity.getCapability(PlayerAnimationCapabilityImpl.PLAYER_ANIMATION).ifPresent(t -> 
					{
						t.setAnimationState(message.animationState);
						t.setPrevAnimationState(message.prevAimationState);
						t.setAnimationTick(message.animationTick);
					});
				});
			}
		});
		ctx.get().setPacketHandled(true);
		return true;
	}
}