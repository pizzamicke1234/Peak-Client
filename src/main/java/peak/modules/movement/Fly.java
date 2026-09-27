package peak.modules.movement;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import org.lwjgl.input.Keyboard;
import peak.events.PacketEvent;
import peak.events.TickEvent;
import peak.managers.DamageManager;
import peak.managers.MovementManager;
import peak.modules.settings.BoolSetting;
import peak.ui.notifications.NotificationManager;
import peak.managers.PacketManager;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;
import peak.modules.settings.NumberSetting;

public class Fly extends Module {

    public ModeSetting flyMode = new ModeSetting("Mode", true, "Motion", "Motion", "Vulcan",
            "Deathzone", "Ground", "NoRules");
    public NumberSetting flySpeed = new NumberSetting("Motion", flyMode, new String[]{"Motion", "NoRules"}, false, 0.25,
            10, 1, 0.25);
    public BoolSetting viewBobbing = new BoolSetting("View Bobbing", false, false);

    public Fly() {
        super("Fly", Keyboard.KEY_Y, Category.MOVEMENT, true);
        addSetting(flyMode, flySpeed, viewBobbing);
    }

    private int ticktimer = 0;
    private double multiplier;
    private boolean hasStarted = false;
    private double firstPosY;

    public void onEnable() {

        ticktimer = 0;

        switch (flyMode.currentValue) {
            case "Vulcan":

                mc.timer.timerSpeed = 0.3f;
                if (getInNearestEntity()) {
                    mc.thePlayer.motionY += 0.5;

                } else {
                    this.toggle();
                }
                break;

            case "Deathzone":
                //DamageManager.damagePlayer(DamageManager.DamageType.POSITION, 2, 3, true, true);
                multiplier = 1.3D;
                hasStarted = true;
                break;

            case "NoRules":
                //DamageManager.damagePlayer(DamageManager.DamageType.POSITION, 10, 50, true, true);
                multiplier = 1;
                mc.thePlayer.motionY = 0.42D;

        }

    }

    public void onDisable() {

        mc.timer.timerSpeed = 1.0f;
        switch (flyMode.currentValue) {
            case "Vulcan":
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionY = 0;
                mc.thePlayer.motionZ = 0;
                break;

            case "Ground":
                break;

            case "Deathzone":
                hasStarted = false;
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionY = 0;
                mc.thePlayer.motionZ = 0;
                break;
        }

        mc.thePlayer.capabilities.isFlying = false;

    }

    public void onTick(TickEvent.TickType tickType) {

        if(tickType == TickEvent.TickType.POST) return;

        if(viewBobbing.isTrue()) {
            mc.thePlayer.cameraYaw = 0.1F;
        }

        ticktimer++;

        switch (flyMode.currentValue) {
            case "Motion":
                motionFly();
                break;

            case "Vulcan":
                vulcanFly();
                break;

            case "Ground":
                groundFly();
                break;

            case "Deathzone":
                motionFly();
                for(int i = 0; i < 10; i++) {
                    Packet packet = new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY,
                            mc.thePlayer.posZ, mc.thePlayer.onGround);
                    PacketManager.sendPacket(packet);
                }
                break;

            case "NoRules":
                noRulesDmg();
                break;
        }

    }

    @Override
    public void onPacket(PacketEvent packetEvent) {

        switch (flyMode.currentValue) {
            case "Deathzone":
                deathzonePacket(packetEvent);
                break;
        }

    }

    public void motionFly() {
        //mc.thePlayer.capabilities.isFlying = true;

        mc.thePlayer.motionY = 0;

        if(ticktimer % 10 == 0) {
            //handleVanillaKickBypass();
        }

        double speed = flySpeed.cValue;
        float yaw = mc.thePlayer.rotationYaw;

        if(mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.thePlayer.motionY += speed / 2;
        }

        if(mc.gameSettings.keyBindSneak.isKeyDown()) {
            mc.thePlayer.motionY -= speed / 2;
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

        double speed = flySpeed.cValue;

        NotificationManager.addChat("Tick | "+ ticktimer);

        if(ticktimer == 2) {
            mc.thePlayer.motionY += 1;
        }

        if(ticktimer < 3) {
            speed = 1;
        }else{
            speed = flySpeed.cValue;
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

    public void noRulesDmg() {
        mc.thePlayer.motionY = 0;
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionZ = 0;

        multiplier = 1.05D - (ticktimer * 0.006D);

        if (multiplier <= 0.51) {
            multiplier = 0.51D;
        }

        float timerOffset = (85 - ticktimer) * 0.01f;
        mc.timer.timerSpeed = (timerOffset > 0.2f) ? 1.0f + timerOffset : 1.2f;

        if(mc.thePlayer.isCollidedHorizontally) {
            multiplier = 0.51D;
        }

        double speed = (flySpeed.cValue / 4D) * multiplier;
        //NotificationManager.addChat("Multi: "+ multiplier + "; Speed: " + speed + "; Timer: " + mc.timer.timerSpeed);
        MovementManager.strafe(speed);
    }

    public void deathzoneFly() {

        mc.thePlayer.motionY = 0;

        if(!hasStarted && mc.thePlayer.hurtTime != 0) {
            hasStarted = true;
        }

        if(mc.gameSettings.keyBindJump.isKeyDown()) {
            mc.thePlayer.motionY += 0.5;
        }

        if(mc.gameSettings.keyBindSneak.isKeyDown()) {
            mc.thePlayer.motionY -= 0.5;
        }

        if(hasStarted) {

            double speed = 0.8;

            if (mc.thePlayer.moveForward != 0 || mc.thePlayer.moveStrafing != 0) {

                MovementManager.strafe(speed + 0.15);

                if(mc.thePlayer.ticksExisted % 10 == 0) {
                    MovementManager.strafe(1);
                }

            }else {
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionZ = 0;
            }

        }

    }

    public void deathzonePacket(PacketEvent packetEvent) {


    }

    public boolean getInNearestEntity() {
        for(Entity e : mc.theWorld.loadedEntityList) {
            if(e instanceof EntityBoat || e instanceof EntityMinecart || e instanceof EntityHorse) {

                if(mc.thePlayer.getDistanceToEntity(e) < 5) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, C02PacketUseEntity.Action.INTERACT));
                    return true;
                }
            }
        }
        NotificationManager.addChat("No rideable Entity found!");
        return false;
    }

}