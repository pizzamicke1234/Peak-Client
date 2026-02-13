package peak.managers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;

public class RotationManager {

    static Minecraft mc = Minecraft.getMinecraft();
    public static float oldYaw, oldPitch;

    public static void lookAtEntity(Entity e, float yawSpeed, float pitchSpeed) {
        float[] targetRotations = getRotationsToEntity(e);

        float currentYaw = mc.thePlayer.rotationYaw;
        float currentPitch = mc.thePlayer.rotationPitch;

        float newYaw = updateRotation(currentYaw, targetRotations[0], yawSpeed);
        float newPitch = updateRotation(currentPitch, targetRotations[1], pitchSpeed);

        mc.thePlayer.rotationYaw = newYaw;
        mc.thePlayer.rotationPitch = newPitch;
    }

    public static void lookAtEntitySilent(Entity e, float yawSpeed, float pitchSpeed, boolean packetSend) {
        float[] targetRotations = getRotationsToEntity(e);

        float currentYaw = mc.thePlayer.rotationYaw;
        float currentPitch = mc.thePlayer.rotationPitch;

        float newYaw = updateRotation(currentYaw, targetRotations[0], yawSpeed);
        float newPitch = updateRotation(currentPitch, targetRotations[1], pitchSpeed);

        mc.thePlayer.renderYawOffset = newYaw;
        mc.thePlayer.setRotationYawHead(newYaw);
        if(packetSend) {
            PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, newYaw, newPitch, mc.thePlayer.onGround));
        }
    }

    public static void lookSilent(float[] rotations, float yawSpeed, float pitchSpeed, boolean packetSend) {
        float[] targetRotations = rotations;

        float currentYaw = mc.thePlayer.rotationYaw;
        float currentPitch = mc.thePlayer.rotationPitch;

        float newYaw = updateRotation(currentYaw, targetRotations[0], yawSpeed);
        float newPitch = updateRotation(currentPitch, targetRotations[1], pitchSpeed);

        mc.thePlayer.renderYawOffset = newYaw;
        mc.thePlayer.setRotationYawHead(newYaw);
        if(packetSend && (newYaw != oldYaw || newPitch != oldPitch)) {
            NotificationManager.addChat("New Rot");
            PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, newYaw, newPitch, mc.thePlayer.onGround));
        }
        oldYaw = newYaw;
        oldPitch = newPitch;
    }

    public static float[] getRotationsToEntity(Entity e) {
        double deltaX = e.posX - mc.thePlayer.posX;
        double deltaZ = e.posZ - mc.thePlayer.posZ;

        double targetEyeHeight = e.getEyeHeight();

        double deltaY = (e.posY + targetEyeHeight) - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());

        double distanceXZ = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        float yaw = (float) (Math.toDegrees(Math.atan2(deltaZ, deltaX)) - 90.0F);
        float pitch = (float) -Math.toDegrees(Math.atan2(deltaY, distanceXZ));

        return new float[]{yaw, pitch};
    }

    public static float[] getScaffoldRotation() {
        return new float[] {mc.thePlayer.rotationYaw - 180, 87};
    }

    public static float updateRotation(float current, float target, float speed) {
        float f = MathHelper.wrapAngleTo180_float(target - current);
        if (f > speed) f = speed;
        if (f < -speed) f = -speed;
        return current + f;
    }

}
