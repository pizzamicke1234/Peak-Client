package peak.events;

import net.minecraft.network.Packet;

import java.util.ArrayList;

public class PacketEvent extends Event{

    public static ArrayList<Packet> toCancelPackets = new ArrayList<>();

    public Packet packet;

    public PacketEvent(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void cancelPacket() {
        toCancelPackets.add(this.packet);
    }

}
