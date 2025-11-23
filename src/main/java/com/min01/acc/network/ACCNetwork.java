package com.min01.acc.network;

import com.min01.acc.AlexsCavesCacophony;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.server.ServerLifecycleHooks;

public class ACCNetwork
{
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(AlexsCavesCacophony.MODID, "accacophony"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);
	
	public static int ID = 0;
	public static void registerMessages()
	{
		CHANNEL.registerMessage(ID++, UpdatePlayerAnimationPacket.class, UpdatePlayerAnimationPacket::encode, UpdatePlayerAnimationPacket::new, UpdatePlayerAnimationPacket.Handler::onMessage);
		CHANNEL.registerMessage(ID++, UpdateItemAnimationPacket.class, UpdateItemAnimationPacket::encode, UpdateItemAnimationPacket::new, UpdateItemAnimationPacket.Handler::onMessage);
		CHANNEL.registerMessage(ID++, UpdateOwnerCapabilityPacket.class, UpdateOwnerCapabilityPacket::encode, UpdateOwnerCapabilityPacket::new, UpdateOwnerCapabilityPacket.Handler::onMessage);
		CHANNEL.registerMessage(ID++, UpdatePaintedCapabilityPacket.class, UpdatePaintedCapabilityPacket::encode, UpdatePaintedCapabilityPacket::new, UpdatePaintedCapabilityPacket.Handler::onMessage);
		CHANNEL.registerMessage(ID++, UpdateOverlayCapabilityPacket.class, UpdateOverlayCapabilityPacket::encode, UpdateOverlayCapabilityPacket::new, UpdateOverlayCapabilityPacket.Handler::onMessage);
	}
	
    public static <MSG> void sendToServer(MSG message) 
    {
    	CHANNEL.sendToServer(message);
    }
    
    public static <MSG> void sendToAll(MSG message)
    {
    	for(ServerPlayer player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) 
    	{
    		CHANNEL.sendTo(message, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    	}
    }
    
    public static <MSG> void sendNonLocal(MSG msg, ServerPlayer player)
    {
    	CHANNEL.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}
