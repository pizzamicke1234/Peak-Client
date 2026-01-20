package peak.modules.movement;

import org.lwjgl.input.Keyboard;
import peak.modules.Module;

public class Fly extends Module {

    public Fly() {
        super("Fly", Keyboard.KEY_Y, Category.MOVEMENT, true);
    }

    public void on_Enable() {
        mc.thePlayer.capabilities.allowFlying = true;
    }

    public void on_Disable() {
        mc.thePlayer.capabilities.isFlying = false;
        mc.thePlayer.capabilities.allowFlying = false;
    }

    public void on_Tick() {
        mc.thePlayer.capabilities.isFlying = true;

        double speed = 2.5;
        float yaw = mc.thePlayer.rotationYaw;

        if(mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.thePlayer.motionY = 0.5;
        }

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
        }else {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        }

    }

}
