package peak.modules.movement;

import org.lwjgl.input.Keyboard;
import peak.modules.Module;

public class Speed extends Module {

    public Speed() {
        super("Speed", Keyboard.KEY_X, Category.MOVEMENT);
    }

    boolean autojump = true;

    public void on_Enable() {

    }

    public void on_Disable() {

    }

    public void on_Tick() {

        if(mc.thePlayer.onGround && autojump){
            mc.thePlayer.jump();
        }

        float yaw = mc.thePlayer.rotationYaw;

        double speed = 0.5;

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
}