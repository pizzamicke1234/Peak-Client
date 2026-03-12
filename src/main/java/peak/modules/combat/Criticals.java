package peak.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import peak.events.AttackEvent;
import peak.events.PacketEvent;
import peak.events.TickEvent;
import peak.managers.PacketManager;
import peak.modules.Module;
import peak.modules.settings.BoolSetting;
import peak.modules.settings.ModeSetting;
import peak.ui.notifications.NotificationManager;

import java.util.Arrays;
import java.util.Collections;

public class Criticals extends Module {

    ModeSetting critMode = new ModeSetting("Mode", true, "Packet", "Packet", "MiniJump", "Deathzone");
    BoolSetting groundOnly = new BoolSetting("Only Ground", false, true);

    public Criticals() {
        super("Criticals", 0, Category.COMBAT, true);
        this.addSetting(critMode, groundOnly);
    }

    private boolean DZshouldCrit = false;

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if(tickType == TickEvent.TickType.POST) return;

        /*if(DZshouldCrit) {
            mc.thePlayer.motionY = -0.1D;
            PacketManager.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                    mc.thePlayer.posY - 0.001D, mc.thePlayer.posZ, false));
            DZshouldCrit = false;
        }*/
    }

    @Override
    public void onAttack(AttackEvent attackEvent) {

        C02PacketUseEntity attack = attackEvent.getAttackPacket();
        Entity entity = attack.getEntityFromWorld(mc.theWorld);
        EntityLivingBase entityLivingBase = (EntityLivingBase) attack.getEntityFromWorld(mc.theWorld);

        if(!mc.thePlayer.onGround && groundOnly.isTrue()) return;
        if(entity.isDead || entityLivingBase.getHealth() < 1 || entityLivingBase.hurtTime > 1) return;

        if (!mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder()) {

            NotificationManager.addChat("Crit");

            switch (critMode.currentValue) {

                case "Packet":
                    double[] offsets = {0.0725, 0.001 - (Math.random() / 10000)};
                    for(double offset : offsets) {
                        PacketManager.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                                mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
                    }
                    break;

                case "MiniJump":
                    PacketManager.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                            mc.thePlayer.posY + 0.03, mc.thePlayer.posZ, false));
                    PacketManager.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                            mc.thePlayer.posY + 0.01, mc.thePlayer.posZ, mc.thePlayer.onGround));
                    break;

                case "Deathzone":

                    if(mc.thePlayer.onGround) {
                        mc.thePlayer.motionY = 0.1D;
                        PacketManager.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                                mc.thePlayer.posY - 0.01D, mc.thePlayer.posZ, false));
                        //DZshouldCrit = true;
                    }else {
                        PacketManager.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                                mc.thePlayer.posY - 0.01D, mc.thePlayer.posZ, false));
                    }

                    break;

            }

        }

    }
}
