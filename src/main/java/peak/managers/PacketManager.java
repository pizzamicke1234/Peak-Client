package peak.managers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

import java.util.ArrayList;

public class PacketManager {

    public static ArrayList<Packet> packetsWithoutEvent = new ArrayList<>();

    static Minecraft mc = Minecraft.getMinecraft();

    public static void sendPacket(Packet packet) {
        if (packet == null) return;
        mc.thePlayer.sendQueue.addToSendQueue(packet);
    }

    public static void sendPacketWithoutEvent(Packet packet) {
        if (packet == null) return;
        packetsWithoutEvent.add(packet);
        mc.thePlayer.sendQueue.addToSendQueue(packet);
    }

    public static void receivePacketWithoutEvent(Packet packet) {
        if (packet == null || mc.getNetHandler() == null) return;
        try {
            // Wir casten das Paket auf den rohen Typ, damit processPacket
            // den NetHandlerPlayClient akzeptiert.
            ((Packet<net.minecraft.network.play.INetHandlerPlayClient>) packet)
                    .processPacket(mc.getNetHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class TimedPacket {
        private final Packet<?> packet;
        private final long time;

        public TimedPacket(Packet<?> packet, long time) {
            this.packet = packet;
            this.time = time;
        }

        public Packet<?> getPacket() { return packet; }
        public long getTime() { return time; }
    }

}

