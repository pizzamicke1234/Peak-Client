package peak.managers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

import java.util.ArrayList;

public class PacketManager {

    public static ArrayList<Packet> packetsWithoutEvent = new ArrayList<>();

    static Minecraft mc = Minecraft.getMinecraft();

    public static void sendPacket(Packet packet) {
        mc.thePlayer.sendQueue.addToSendQueue(packet);
    }

    public static void sendPacketWithoutEvent(Packet packet) {
        packetsWithoutEvent.add(packet);
        mc.thePlayer.sendQueue.addToSendQueue(packet);
    }

}
