package peak.modules.combat;

import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import peak.events.PacketEvent;
import peak.managers.PacketManager;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;

public class Criticals extends Module {

    ModeSetting critMode = new ModeSetting("Mode", true, "Packet", "Packet", "MiniJump");

    public Criticals() {
        super("Criticals", 0, Category.COMBAT, true);
        this.addSetting(critMode);
    }

    @Override
    public void onPacket(PacketEvent packetEvent) {
        if(packetEvent.getType() != PacketEvent.Type.SEND) return;

        if (packetEvent.getPacket() instanceof C02PacketUseEntity) {
            C02PacketUseEntity attack = (C02PacketUseEntity) packetEvent.getPacket();

            if (attack.getAction() == C02PacketUseEntity.Action.ATTACK) {
                if (mc.thePlayer.onGround && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder()) {

                    switch (critMode.currentValue) {

                        case "Packet":
                            double[] offsets = {0.0725, 0.001 - (Math.random() / 10000)};
                            for(double offset : offsets) {
                                PacketManager.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                                        mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
                            }
                            break;

                        case "MiniJump":
                            mc.thePlayer.motionY = 0.001;
                            break;

                    }

                }
            }

        }

    }
}
