package peak.modules.combat;

import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S32PacketConfirmTransaction;
import peak.events.PacketEvent;
import peak.managers.NotificationManager;
import peak.managers.PacketManager;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;

public class Velocity extends Module {

    public ModeSetting velocityMode = new ModeSetting("Mode", true, "Vanilla", "Vanilla", "Vulcan");

    public Velocity() {
        super("Velocity", 0, Category.COMBAT, true);
        addSetting(velocityMode);
    }

    boolean transaction = true;

    @Override
    public void onPacket(PacketEvent packetEvent) {

        switch(velocityMode.current_value) {

            case "Vanilla":
                if(packetEvent.getPacket() instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity) packetEvent.getPacket();
                    if (mc.thePlayer != null && packet.getEntityID() == mc.thePlayer.getEntityId()) {
                        packetEvent.cancelPacket();
                    }
                }
                break;

            case "Vulcan":

                int i;
                if(transaction) {
                    i = 1;
                }else {
                    i = -1;
                }

                if(packetEvent.getPacket() instanceof S32PacketConfirmTransaction) {
                    PacketManager.sendPacketWithoutEvent(new C0FPacketConfirmTransaction(i, (short)(i * -1), transaction));
                    packetEvent.cancelPacket();
                    transaction = !transaction;
                }

                if(packetEvent.getPacket() instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity packet = (S12PacketEntityVelocity) packetEvent.getPacket();
                    if (mc.thePlayer != null && packet.getEntityID() == mc.thePlayer.getEntityId()) {
                        packetEvent.cancelPacket();
                    }
                }
                break;
        }

    }

}
