package peak.events;

import net.minecraft.network.Packet;
import java.util.ArrayList;

public class PacketEvent extends Event {

    public static ArrayList<Packet> toCancelPackets = new ArrayList<>();

    public Packet packet;
    public boolean isCanceled;
    private final Type type;

    public enum Type {
        SEND,    // Client -> Server
        RECEIVE  // Server -> Client
    }

    public PacketEvent(Packet packet, Type type) {
        this.packet = packet;
        this.type = type;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public Type getType() {
        return this.type;
    }

    public void cancelPacket() {
        this.isCanceled = true;
        toCancelPackets.add(this.packet);
    }

    public boolean isCanceled() {
        return isCanceled;
    }
}