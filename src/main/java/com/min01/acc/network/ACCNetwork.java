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
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(ResourceLocation.fromNamespaceAndPath(AlexsCavesCacophony.MODID, AlexsCavesCacophony.MODID),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);
	
	public static int ID = 0;
	public static void registerMessages()
	{
		CHANNEL.registerMessage(ID++, UpdatePlayerAnimationPacket.class, UpdatePlayerAnimationPacket::write, UpdatePlayerAnimationPacket::read, UpdatePlayerAnimationPacket::handle);
		CHANNEL.registerMessage(ID++, UpdateItemAnimationPacket.class, UpdateItemAnimationPacket::write, UpdateItemAnimationPacket::read, UpdateItemAnimationPacket::handle);
		CHANNEL.registerMessage(ID++, UpdateOwnerCapabilityPacket.class, UpdateOwnerCapabilityPacket::write, UpdateOwnerCapabilityPacket::read, UpdateOwnerCapabilityPacket::handle);
		CHANNEL.registerMessage(ID++, UpdatePaintedCapabilityPacket.class, UpdatePaintedCapabilityPacket::write, UpdatePaintedCapabilityPacket::read, UpdatePaintedCapabilityPacket::handle);
		CHANNEL.registerMessage(ID++, UpdateOverlayCapabilityPacket.class, UpdateOverlayCapabilityPacket::write, UpdateOverlayCapabilityPacket::read, UpdateOverlayCapabilityPacket::handle);
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
