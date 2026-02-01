package peak.modules.movement;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Keyboard;
import peak.events.PacketEvent;
import peak.events.TickEvent;
import peak.managers.DamageManager;
import peak.managers.MovementManager;
import peak.managers.NotificationManager;
import peak.managers.PacketManager;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;
import peak.modules.settings.NumberSetting;

public class Fly extends Module {

    public ModeSetting flyMode = new ModeSetting("Mode", true, "Motion", "Motion", "Vulcan", "Deathzone", "Deathzone Exp", "Ground");
    public NumberSetting motionsetting = new NumberSetting("Motion", false, 0.25,
            10, 1, 0.25);

    public Fly() {
        super("Fly", Keyboard.KEY_Y, Category.MOVEMENT, true);
        addSetting(flyMode, motionsetting);
    }

    public int ticktimer = 0;
    //Deathzone
    int deathzoneFlyTicks = 0;
    int dmgJumpCount = 0;
    boolean hasStarted = false;
    boolean waitFlag = false;
    boolean packetCancel = true;
    double lastSentX;
    double lastSentY;
    double lastSentZ;
    double lastTickX = 0;
    double lastTickY = 0;
    double lastTickZ = 0;
    double firstPosY;

    public void onEnable() {

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

            case "Deathzone":
                deathzoneFlyTicks = 0;
                waitFlag = false;
                hasStarted = false;
                dmgJumpCount = 11451;
                DamageManager.damagePlayer(DamageManager.DamageType.OLDVULCAN, 1, 1, true, true);
                waitFlag = true;
                break;

            case "Deathzone Exp":
                hasStarted = true;
                firstPosY = mc.thePlayer.posY;
                DamageManager.damagePlayer(DamageManager.DamageType.OLDVULCAN, 1, 1, true, true);
                break;
        }

    }

    public void onDisable() {

        mc.timer.timerSpeed = 1.0f;
        switch (flyMode.current_value) {
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
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionY = 0;
                mc.thePlayer.motionZ = 0;
                break;

            case "Deathzone Exp":
                PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
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

        //ticktimer++;

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

            case "Deathzone":
                deathzoneFly();
                break;

            case "Deathzone Exp":
                deathzoneExpFly();
                break;
        }

    }

    @Override
    public void onPacket(PacketEvent packetEvent) {

        switch (flyMode.current_value) {
            case "Deathzone":
                deathzonePacket(packetEvent);
                break;

            case "Deathzone Exp":
                deathzoneExpPacket(packetEvent);
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

    public void deathzoneFly() {

        if(hasStarted){
            ticktimer++;
            NotificationManager.addChat(("[" + ticktimer + "]"));
        }

        if(ticktimer > 29) {
            this.toggle();
            return;
        }

        if(dmgJumpCount == 11451) {
            if(!hasStarted){
                return;
            }
            else {
                hasStarted = true;
                waitFlag = false;
                //bomba
                PacketManager.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                dmgJumpCount = 999;
            }
        }
        mc.thePlayer.jumpMovementFactor = 0.00f;
        if (!hasStarted && !waitFlag) {
            PacketManager.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.0784, mc.thePlayer.posZ, false));
            waitFlag = true;
        }

        if(hasStarted) {
            mc.timer.timerSpeed = 0.4f;
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionY = 0;
            mc.thePlayer.motionZ = 0;
            if (!mc.gameSettings.keyBindSneak.isKeyDown()) {
                mc.thePlayer.motionX = 0;
                mc.thePlayer.motionY = 0;
                mc.thePlayer.motionZ = 0;
            }
            if (mc.gameSettings.keyBindJump.isKeyDown()) {
                mc.thePlayer.motionY = 0.1;
            }

            MovementManager.strafe(motionsetting.cValue / 2);
        }

    }

    public void deathzonePacket(PacketEvent packetEvent) {
        Packet packet = packetEvent.getPacket();

        if (packet instanceof C03PacketPlayer && waitFlag) {
            packetEvent.cancelPacket();
        }
        if (packet instanceof C03PacketPlayer) {
            //packet.set
        }
        if(hasStarted) {
            if(packet instanceof C03PacketPlayer && (packet instanceof C03PacketPlayer.C04PacketPlayerPosition || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook)) {
                double deltaX = ((C03PacketPlayer) packet).getPositionX() - lastSentX;
                double deltaY = ((C03PacketPlayer) packet).getPositionY() - lastSentY;
                double deltaZ = ((C03PacketPlayer) packet).getPositionZ() - lastSentZ;

                if (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) > 10) {
                    deathzoneFlyTicks++;
                    PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(lastTickX, lastTickY, lastTickZ, false));
                    lastSentX = lastTickX;
                    lastSentY = lastTickY;
                    lastSentZ = lastTickZ;
                }
                lastTickX = ((C03PacketPlayer) packet).getPositionX();
                lastTickY = ((C03PacketPlayer) packet).getPositionY();
                lastTickZ = ((C03PacketPlayer) packet).getPositionZ();
                packetEvent.cancelPacket();
            }else if(packet instanceof C03PacketPlayer) {
                packetEvent.cancelPacket();
            }
        }

        if(packet instanceof S08PacketPlayerPosLook) {
            hasStarted = true;
            waitFlag = false;
        }

        if (packet instanceof S08PacketPlayerPosLook) {
            lastSentX = ((S08PacketPlayerPosLook) packet).getX();
            lastSentY = ((S08PacketPlayerPosLook) packet).getY();
            lastSentZ = ((S08PacketPlayerPosLook) packet).getZ();

            PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(((S08PacketPlayerPosLook) packet).getX(),
                    ((S08PacketPlayerPosLook) packet).getY(), ((S08PacketPlayerPosLook) packet).getZ(),
                    ((S08PacketPlayerPosLook) packet).getYaw(), ((S08PacketPlayerPosLook) packet).getPitch(),
                    false));
        }

        if (packet instanceof C0FPacketConfirmTransaction) { //Make sure it works with Vulcan Velocity
            int transUID = (((C0FPacketConfirmTransaction) packet).getUid());
            if (transUID >= -31767 && transUID <= -30769) {
                packetEvent.cancelPacket();
                PacketManager.sendPacketWithoutEvent(packet);
            }
        }
    }

    public void deathzoneExpFly() {

        if(hasStarted) {
            mc.thePlayer.motionY = 0;
            MovementManager.strafe(0.2);
        }

        if(mc.thePlayer.posY < firstPosY) {
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.05, mc.thePlayer.posZ);
        }

        if(mc.thePlayer.ticksExisted % 5 == 0) {
            if(packetCancel) {
                //PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.15, mc.thePlayer.posZ);
                packetCancel = !packetCancel;
            }else {
                //PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.15, mc.thePlayer.posZ);

                packetCancel = !packetCancel;
            }
        }

    }

    public void deathzoneExpPacket(PacketEvent packetEvent) {

        Packet packet = packetEvent.getPacket();

        if (packet instanceof C03PacketPlayer || packet instanceof C0FPacketConfirmTransaction || packet instanceof C00PacketKeepAlive) {
            packetEvent.cancelPacket();
        }
        if (packet instanceof C03PacketPlayer) {
            //packet.set
        }
        if(hasStarted) {
            if(packet instanceof C03PacketPlayer && (packet instanceof C03PacketPlayer.C04PacketPlayerPosition || packet instanceof C03PacketPlayer.C06PacketPlayerPosLook)) {
                double deltaX = ((C03PacketPlayer) packet).getPositionX() - lastSentX;
                double deltaY = ((C03PacketPlayer) packet).getPositionY() - lastSentY;
                double deltaZ = ((C03PacketPlayer) packet).getPositionZ() - lastSentZ;

                if (Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) > 10) {
                    deathzoneFlyTicks++;
                    PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(lastTickX, lastTickY, lastTickZ, true));
                    PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(lastTickX, lastTickY, lastTickZ, true));
                    lastSentX = lastTickX;
                    lastSentY = lastTickY;
                    lastSentZ = lastTickZ;
                }
                lastTickX = ((C03PacketPlayer) packet).getPositionX();
                lastTickY = ((C03PacketPlayer) packet).getPositionY();
                lastTickZ = ((C03PacketPlayer) packet).getPositionZ();
                packetEvent.cancelPacket();
            }else if(packet instanceof C03PacketPlayer) {
                packetEvent.cancelPacket();
            }
        }

        if(packet instanceof S08PacketPlayerPosLook) {
            hasStarted = true;
            waitFlag = false;
        }

        if (packet instanceof S08PacketPlayerPosLook) {
            lastSentX = ((S08PacketPlayerPosLook) packet).getX();
            lastSentY = ((S08PacketPlayerPosLook) packet).getY();
            lastSentZ = ((S08PacketPlayerPosLook) packet).getZ();

            PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(((S08PacketPlayerPosLook) packet).getX(),
                    ((S08PacketPlayerPosLook) packet).getY(), ((S08PacketPlayerPosLook) packet).getZ(),
                    ((S08PacketPlayerPosLook) packet).getYaw(), ((S08PacketPlayerPosLook) packet).getPitch(),
                    false));
        }

        if (packet instanceof C0FPacketConfirmTransaction) { //Make sure it works with Vulcan Velocity
            int transUID = (((C0FPacketConfirmTransaction) packet).getUid());
            if (transUID >= -31767 && transUID <= -30769) {
                packetEvent.cancelPacket();
                PacketManager.sendPacketWithoutEvent(packet);
            }
        }

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