package peak.managers;

//Skidded by Rise client hehehe

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;

public class DamageManager {

    private static Minecraft mc = Minecraft.getMinecraft();

    public static void damagePlayer(final DamageType type, final double value, final boolean groundCheck, final boolean hurtTimeCheck) {
        if ((!groundCheck || mc.thePlayer.onGround) && (!hurtTimeCheck || mc.thePlayer.hurtTime == 0)) {
            final double x = mc.thePlayer.posX;
            final double y = mc.thePlayer.posY;
            final double z = mc.thePlayer.posZ;

            double fallDistanceReq = 3.1;

            if (mc.thePlayer.isPotionActive(Potion.jump)) {
                final int amplifier = mc.thePlayer.getActivePotionEffect(Potion.jump).getAmplifier();
                fallDistanceReq += (float) (amplifier + 1);
            }

            final int packetCount = (int) Math.ceil(fallDistanceReq / value); // Don't change this unless you know the change wont break the self damage.
            for (int i = 0; i < packetCount; i++) {
                switch (type) {
                    case POSITION_ROTATION:
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(x, y + value, z, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(x, y, z, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
                        break;


                    case POSITION:
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + value, z, false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                        break;

                    case OLDVULCAN:
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.0784, mc.thePlayer.posZ, false));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.41999998688697815, mc.thePlayer.posZ, false));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.7531999805212, mc.thePlayer.posZ, false));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0, mc.thePlayer.posZ, true));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.4199999868869781, mc.thePlayer.posZ, false));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.7531999805212, mc.thePlayer.posZ, false));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 2.0, mc.thePlayer.posZ, true));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 2.419999986886978, mc.thePlayer.posZ, false));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 2.7531999805212, mc.thePlayer.posZ, false));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.00133597911214, mc.thePlayer.posZ, false));
                        //mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.00133597911214, mc.thePlayer.posZ);
                        break;
                }
            }
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        }
    }

    public static void damagePlayer(final DamageType type, final double value, final int packets, final boolean groundCheck, final boolean hurtTimeCheck) {
        if ((!groundCheck || mc.thePlayer.onGround) && (!hurtTimeCheck || mc.thePlayer.hurtTime == 0)) {
            final double x = mc.thePlayer.posX;
            final double y = mc.thePlayer.posY;
            final double z = mc.thePlayer.posZ;

            for (int i = 0; i < packets; i++) {
                switch (type) {
                    case POSITION_ROTATION:
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(x, y + value, z, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(x, y, z, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, false));
                        break;


                    case POSITION:
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + value, z, false));
                        mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
                        break;

                    case OLDVULCAN:
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.0784, mc.thePlayer.posZ, false));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.41999998688697815, mc.thePlayer.posZ, false));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.7531999805212, mc.thePlayer.posZ, false));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0, mc.thePlayer.posZ, true));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.4199999868869781, mc.thePlayer.posZ, false));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.7531999805212, mc.thePlayer.posZ, false));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 2.0, mc.thePlayer.posZ, true));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 2.419999986886978, mc.thePlayer.posZ, false));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 2.7531999805212, mc.thePlayer.posZ, false));
                        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.00133597911214, mc.thePlayer.posZ, false));
                        //mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 3.00133597911214, mc.thePlayer.posZ);
                        break;

                }
            }
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
        }
    }

    public enum DamageType {
        POSITION_ROTATION,
        POSITION,
        OLDVULCAN,
        OLDVULCANJUMP
    }

}
