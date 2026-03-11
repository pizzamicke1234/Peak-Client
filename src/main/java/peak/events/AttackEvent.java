package peak.events;

import net.minecraft.network.play.client.C02PacketUseEntity;

public class AttackEvent extends Event{

    C02PacketUseEntity attackPacket;

    public AttackEvent(C02PacketUseEntity attackPacket) {
        this.attackPacket = attackPacket;
    }

    public C02PacketUseEntity getAttackPacket() {
        return attackPacket;
    }
}
