package peak.managers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class MovementManager {

    static Minecraft mc = Minecraft.getMinecraft();

    public static double getSpeed() {
        // nigga hypot heavy
        return Math.hypot(mc.thePlayer.motionX, mc.thePlayer.motionZ);
    }

    public static boolean isPressingMove() {
        return mc.gameSettings.keyBindForward.isKeyDown() || mc.gameSettings.keyBindBack.isKeyDown() ||
                mc.gameSettings.keyBindLeft.isKeyDown() || mc.gameSettings.keyBindRight.isKeyDown();
    }

    public static boolean isMoving() {
        return mc.thePlayer != null && (mc.thePlayer.movementInput.moveForward != 0F || mc.thePlayer.movementInput.moveStrafe != 0F);
    }

    public static void strafe(double speed) {

        float yaw = mc.thePlayer.rotationYaw;

        if (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {

            if (mc.thePlayer.moveForward < 0) {
                yaw += 180;
            }

            if (mc.thePlayer.moveStrafing > 0) {
                yaw -= 90 * (mc.thePlayer.moveForward > 0 ? 0.5f : (mc.thePlayer.moveForward < 0 ? -0.5f : 1));
            } else if (mc.thePlayer.moveStrafing < 0) {
                yaw += 90 * (mc.thePlayer.moveForward > 0 ? 0.5f : (mc.thePlayer.moveForward < 0 ? -0.5f : 1));
            }

            double rad = Math.toRadians(yaw);
            mc.thePlayer.motionX = -Math.sin(rad) * speed;
            mc.thePlayer.motionZ = Math.cos(rad) * speed;
        }

    }

    public static double getDirection(final float yaw) {
        float rotationYaw = yaw;

        if (EntityPlayer.movementYaw != null) {
            rotationYaw = EntityPlayer.movementYaw;
        }

        if (mc.thePlayer.moveForward < 0F) rotationYaw += 180F;

        float forward = 1F;

        if (mc.thePlayer.moveForward < 0F) forward = -0.5F;
        else if (mc.thePlayer.moveForward > 0F) forward = 0.5F;

        if (mc.thePlayer.moveStrafing > 0F) rotationYaw -= 90F * forward;
        if (mc.thePlayer.moveStrafing < 0F) rotationYaw += 90F * forward;

        return Math.toRadians(rotationYaw);
    }

}
