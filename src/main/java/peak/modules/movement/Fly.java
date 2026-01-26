package peak.modules.movement;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import org.lwjgl.input.Keyboard;
import peak.managers.NotificationManager;
import peak.managers.PacketManager;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;
import peak.modules.settings.NumberSetting;
import peak.tickevents.TickEvent;

public class Fly extends Module {

    public ModeSetting flyMode = new ModeSetting("Mode", true, "Motion", "Motion", "Vulcan", "Ground");
    public NumberSetting motionsetting = new NumberSetting("Motion", false, 0.25,
            10, 1, 0.25);

    public Fly() {
        super("Fly", Keyboard.KEY_Y, Category.MOVEMENT, true);
        addSetting(flyMode, motionsetting);
    }

    private int ticktimer = 0;

    public void on_Enable() {

        ticktimer = 0;

        switch (flyMode.current_value) {
            case "Vulcan":

                mc.timer.timerSpeed = 0.3f;
                if (getInNearestEntity()) {
                    mc.thePlayer.motionY += 0.5;

                } else {
                    this.toggle();
                }
                break;

            case "Ground":
                //PacketManager.cancelPacketType(C0FPacketConfirmTransaction.class);
                break;
        }

    }

    public void on_Disable() {

        switch (flyMode.current_value) {
            case "Vulcan":
                //mc.timer.timerSpeed = 1.0f;
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionY = 0;
                mc.thePlayer.motionZ = 0;
                mc.timer.timerSpeed = 1.0f;
                break;

            case "Ground":
                //PacketManager.uncancelPacketType(C0FPacketConfirmTransaction.class);
                break;
        }

        mc.thePlayer.capabilities.isFlying = false;

    }

    public void on_Tick(TickEvent.TickType tickType) {

        if(tickType == TickEvent.TickType.POST) return;

        ticktimer++;
        //NotificationManager.addChat(String.valueOf(ticktimer));

        switch (flyMode.current_value) {
            case "Motion":
                motionFly();
                break;

            case "Vulcan":
                vulcanFly();
                break;

            case "Ground":
                groundFly();
                break;
        }

    }

    public void motionFly() {
        mc.thePlayer.capabilities.isFlying = true;

        double speed = motionsetting.cValue;
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

    public void vulcanFly() {

        double speed = motionsetting.cValue;

        NotificationManager.addChat("Tick | "+ ticktimer);

        if(ticktimer == 2) {
            mc.thePlayer.motionY += 1;
        }

        if(ticktimer < 3) {
            speed = 1;
        }else{
            speed = motionsetting.cValue;
        }

        if(ticktimer >= 21){
            this.toggle();
            return;
        }

        mc.thePlayer.capabilities.isFlying = true;

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

    public void groundFly() {
        mc.thePlayer.onGround = true;
        mc.thePlayer.motionY = 0;
    }

    public boolean getInNearestEntity() {
        for(Entity e : mc.theWorld.loadedEntityList) {
            if(e instanceof EntityBoat || e instanceof EntityMinecart || e instanceof EntityHorse) {

                if(mc.thePlayer.getDistanceToEntity(e) < 5) {
                PacketManager.sendPacket(new C02PacketUseEntity(e, C02PacketUseEntity.Action.INTERACT));
                return true;
                }
            }
        }
        NotificationManager.addChat("No rideable Entity found!");
        return false;
    }

}
