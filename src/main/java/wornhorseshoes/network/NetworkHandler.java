package wornhorseshoes.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import wornhorseshoes.WornHorseshoes;

public class NetworkHandler {
	private static final SimpleNetworkWrapper dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel(WornHorseshoes.MODID);
	private static byte packetId = 0;

	public static void registerPackets() {
		dispatcher.registerMessage(ToServerSetRearingPacket.ToServerSetRearingPacketHandler.class, ToServerSetRearingPacket.class, packetId++, Side.SERVER);
	}

	public static void sendToServer(IMessage message) {
		NetworkHandler.dispatcher.sendToServer(message);
	}
}
