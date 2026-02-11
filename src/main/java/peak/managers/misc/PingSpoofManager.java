package peak.managers.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.*;
import peak.events.PacketEvent;
import peak.managers.PacketManager;
import java.util.concurrent.ConcurrentLinkedQueue;

public class PingSpoofManager {
    // Queues für die verzögerten Pakete
    public final ConcurrentLinkedQueue<PacketManager.TimedPacket> incomingPackets = new ConcurrentLinkedQueue<>();
    public final ConcurrentLinkedQueue<PacketManager.TimedPacket> outgoingPackets = new ConcurrentLinkedQueue<>();

    public static boolean spoofing;
    public static int delay = 100; // Beispiel: 100ms

    // Einstellungen, welche Pakete verzögert werden sollen
    public static boolean normal = true, teleport = true, velocity = true;

    public void onTick() {
        if (Minecraft.getMinecraft().thePlayer == null) return;

        long now = System.currentTimeMillis();

        // Eingehende Pakete freigeben
        incomingPackets.removeIf(tp -> {
            if (now > tp.getTime() + delay) {
                PacketManager.receivePacketWithoutEvent(tp.getPacket());
                return true;
            }
            return false;
        });

        // Ausgehende Pakete freigeben
        outgoingPackets.removeIf(tp -> {
            if (now > tp.getTime() + delay) {
                PacketManager.sendPacketWithoutEvent(tp.getPacket());
                return true;
            }
            return false;
        });
    }

    public void handlePacket(PacketEvent event) {
        if (!spoofing) return;
        Packet<?> packet = event.getPacket();

        // 1. Ausgehende Pakete (C-Pakete)
        if (event.getType() == PacketEvent.Type.SEND) {
            if (packet instanceof C03PacketPlayer || packet instanceof C0FPacketConfirmTransaction) {
                outgoingPackets.add(new PacketManager.TimedPacket(packet, System.currentTimeMillis()));
                event.cancelPacket();
            }
        }
        // 2. Eingehende Pakete (S-Pakete)
        else if (event.getType() == PacketEvent.Type.RECEIVE) {
            if (packet instanceof S00PacketKeepAlive || packet instanceof S32PacketConfirmTransaction) {
                incomingPackets.add(new PacketManager.TimedPacket(packet, System.currentTimeMillis()));
                event.cancelPacket();
            }
        }
    }
}