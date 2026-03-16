package peak.modules.movement;

import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import org.lwjgl.input.Keyboard;
import peak.events.PacketEvent;
import peak.managers.MovementManager;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;
import peak.events.TickEvent;
import peak.modules.settings.NumberSetting;

public class Speed extends Module {

    public static ModeSetting speedMode = new ModeSetting("Mode", true, "Motion", "Motion", "VulcanYPort", "Advancius");
    NumberSetting speedValue = new NumberSetting("Speed", false, 0.1, 1, 0.5, 0.1);

    public Speed() {
        super("Speed", Keyboard.KEY_X, Category.MOVEMENT, true);
        addSetting(speedMode, speedValue);
    }

    boolean autojump = true;
    double jumpPos;
    boolean jumptoggle = true;

    public void onEnable() {

    }

    public void onDisable() {
        mc.timer.timerSpeed = 1f;
    }

    public void onTick(TickEvent.TickType tickType) {

        if(tickType == TickEvent.TickType.POST) return;

        switch (speedMode.currentValue) {

            case "Motion":
                motionSpeed();
                break;

            case "VulcanYPort":
                vulcanYPort();
                break;

            case "Advancius":
                advanicus();
                break;
        }

    }

    @Override
    public void onPacket(PacketEvent packetEvent) {

        switch (speedMode.currentValue) {
            case "VulcanYPort":
                if(mc.thePlayer.posY >= jumpPos + 1 && jumptoggle) {
                    if(packetEvent.getPacket() instanceof C0FPacketConfirmTransaction) {
                        packetEvent.cancelPacket();
                    }
                }
                break;

            case "Advanicus":
                if(mc.thePlayer.posY >= jumpPos && jumptoggle) {
                    if(packetEvent.getPacket() instanceof C0FPacketConfirmTransaction) {
                        packetEvent.cancelPacket();
                    }
                }
                break;
        }

    }

    public void motionSpeed() {
        if(!MovementManager.isPressingMove()) return;

        if(mc.thePlayer.onGround && autojump){
            mc.thePlayer.jump();
        }

        MovementManager.strafe(speedValue.cValue);
    }

    public void vulcanYPort() {

        if(mc.thePlayer.onGround) {
            jumpPos = mc.thePlayer.posY;
            mc.thePlayer.jump();
            jumptoggle = !jumptoggle;
        }

        if(mc.thePlayer.posY >= jumpPos + 1 && jumptoggle) {
            mc.thePlayer.motionY *= -0.41;
            jumpPos = mc.thePlayer.posY;
        }
        /*float yaw = mc.thePlayer.rotationYaw;
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
        }*/

    }

    public void advanicus() {

        if(mc.thePlayer.onGround) {
            mc.timer.timerSpeed = 1f;
            jumpPos = mc.thePlayer.posY;
            mc.thePlayer.jump();
            jumptoggle = !jumptoggle;
        }

        if(mc.thePlayer.posY >= jumpPos + 1 && jumptoggle) {
            mc.thePlayer.motionY *= -0.41;
            mc.timer.timerSpeed = 3f;
            jumpPos = mc.thePlayer.posY;
        }



    }

}