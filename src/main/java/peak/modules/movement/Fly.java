package peak.modules.movement;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;
import peak.events.PacketEvent;
import peak.events.TickEvent;
import peak.managers.MovementManager;
import peak.modules.settings.BoolSetting;
import peak.ui.notifications.NotificationManager;
import peak.managers.PacketManager;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;
import peak.modules.settings.NumberSetting;

public class Fly extends Module {

    public ModeSetting flyMode = new ModeSetting("Mode", true, "Motion", "Motion", "Vulcan", "Deathzone", "Ground");
    public NumberSetting motionsetting = new NumberSetting("Motion", false, 0.25,
            10, 1, 0.25);
    public BoolSetting viewBobbing = new BoolSetting("View Bobbing", false, false);

    public Fly() {
        super("Fly", Keyboard.KEY_Y, Category.MOVEMENT, true);
        addSetting(flyMode, motionsetting, viewBobbing);
    }

    private int ticktimer = 0;
    private long groundTimer;
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
                hasStarted = true;
                mc.thePlayer.capabilities.allowFlying = true;
                firstPosY = mc.thePlayer.posY;
                break;

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
                PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
                hasStarted = false;
                mc.thePlayer.capabilities.allowFlying = false;
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
                deathzoneFly();
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
            handleVanillaKickBypass();
        }

        double speed = motionsetting.cValue;
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

    public void deathzoneFly() {

        if(hasStarted) {

            mc.timer.timerSpeed = 0.3f;
            mc.thePlayer.motionY = 0.000D;
            C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + 1, mc.thePlayer.posY, mc.thePlayer.posZ, false);
            PacketManager.sendPacketWithoutEvent(packet);
            mc.thePlayer.setPosition(mc.thePlayer.posX + 1, mc.thePlayer.posY, mc.thePlayer.posZ);

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

    private void handleVanillaKickBypass() {
        if (System.currentTimeMillis() - groundTimer < 1000) return;

        final double x = mc.thePlayer.posX;
        final double y = mc.thePlayer.posY;
        final double z = mc.thePlayer.posZ;

        final double ground = calculateGround();

        for (double posY = y; posY > ground; posY -= 8D) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, posY, z, true));

            if (posY - 8D < ground) break; // Prevent next step
        }

        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, ground, z, true));


        for (double posY = ground; posY < y; posY += 8D) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, posY, z, true));

            if (posY + 8D > y) break; // Prevent next step
        }

        mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, true));

        groundTimer = System.currentTimeMillis();
    }

    public double calculateGround() {
        final double y = mc.thePlayer.posY;

        final AxisAlignedBB playerBoundingBox = mc.thePlayer.getEntityBoundingBox();
        double blockHeight = 1D;

        for (double ground = y; ground > 0D; ground -= blockHeight) {
            final AxisAlignedBB customBox = new AxisAlignedBB(playerBoundingBox.maxX, ground + blockHeight, playerBoundingBox.maxZ, playerBoundingBox.minX, ground, playerBoundingBox.minZ);

            if (mc.theWorld.checkBlockCollision(customBox)) {
                if (blockHeight <= 0.05D)
                    return ground + blockHeight;

                ground += blockHeight;
                blockHeight = 0.05D;
            }
        }

        return 0F;
    }

}