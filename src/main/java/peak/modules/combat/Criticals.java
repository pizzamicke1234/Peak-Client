package peak.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import peak.events.AttackEvent;
import peak.events.PacketEvent;
import peak.events.TickEvent;
import peak.managers.PacketManager;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;
import peak.ui.notifications.NotificationManager;

import java.util.Arrays;
import java.util.Collections;

public class Criticals extends Module {

    ModeSetting critMode = new ModeSetting("Mode", true, "Packet", "Packet", "MiniJump", "Deathzone");

    public Criticals() {
        super("Criticals", 0, Category.COMBAT, true);
        this.addSetting(critMode);
    }

    @Override
    public void onAttack(AttackEvent attackEvent) {

        C02PacketUseEntity attack = attackEvent.getAttackPacket();
        Entity entity = attack.getEntityFromWorld(mc.theWorld);

        if(entity.isDead || entity.hurtResistantTime > 11) return;

        if (mc.thePlayer.onGround && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder()) {

            NotificationManager.addChat("Crit event fired");

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
                    mc.thePlayer.motionY = 1;
                    break;

                case "Deathzone":
                    double[] deathzoneOffsets = {0.0125, 0.0250, 0.0375, 0.0500};
                    for(double offset : deathzoneOffsets) {
                        PacketManager.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                                mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
                    }
                    for(int i = deathzoneOffsets.length - 1; i >= 0; i--) {
                        PacketManager.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                                mc.thePlayer.posY + deathzoneOffsets[i], mc.thePlayer.posZ, false));
                    }
                    break;

            }

        }

    }
}
