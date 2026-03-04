package peak.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import org.lwjgl.input.Keyboard;
import peak.events.PacketEvent;
import peak.events.TickEvent;
import peak.managers.PacketManager;
import peak.managers.render.HitBox;
import peak.managers.render.RenderManager;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;
import peak.modules.settings.NumberSetting;
import peak.ui.notifications.Notification;
import peak.ui.notifications.NotificationManager;

import java.util.ArrayList;
import java.util.Collections;

public class InfiniteAura extends Module {

    ModeSetting mode = new ModeSetting("Mode", false, "Vanilla", "Vanilla", "Deathzone Boat", "Deathzone Exp");
    NumberSetting range = new NumberSetting("Range", false, 10, 100, 30, 5);
    NumberSetting cps = new NumberSetting("CPS", false, 1, 20, 3, 1);

    public InfiniteAura() {
        super("InfiniteAura", Keyboard.KEY_I, Category.COMBAT, true);
        this.addSetting(mode, range, cps);
    }

    Entity selecetedEntity;
    boolean allowTarget = true;
    boolean hitDone = false;
    ArrayList<C03PacketPlayer.C04PacketPlayerPosition> tpPackets = new ArrayList<>();
    ArrayList<C03PacketPlayer.C04PacketPlayerPosition> tpPacketsDeathzone = new ArrayList<>();

    ArrayList<C03PacketPlayer.C04PacketPlayerPosition> exemptPackets = new ArrayList<>();

    @Override
    public void onEnable() {
        selecetedEntity = getClosestEntity();

        if(mode.currentValue.equals("Deathzone Boat")) {
            if(mc.thePlayer.ridingEntity == null) {
                Notification notification = new Notification("Warning", "Only enable when in a boat",
                        Notification.NotificationType.WARNING, 3000);
                NotificationManager.addNotification(notification);
                this.toggle();
            }
        }

    }

    @Override
    public void onDisable() {
        selecetedEntity = null;
        tpPackets.clear();
        RenderManager.hitboxes.clear();
        allowTarget = true;
        hitDone = false;
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if(tickType == TickEvent.TickType.POST) return;

        //Target selection
        if(selecetedEntity == null || selecetedEntity.isDead) {
            selecetedEntity = getClosestEntity();
        }
        if(selecetedEntity == null) return;

        //Packet hit

        if(mode.currentValue.equals("Deathzone Boat")) {

            if(mc.thePlayer.ridingEntity == null) {

                if(tpPackets.isEmpty()) {
                    tpPackets = getPacketsToEntity(selecetedEntity);
                }

                RenderManager.hitboxes.clear();
                hitEntity(selecetedEntity, tpPackets, true);
                tpPackets.clear();

                getInNearestEntity();
            }
            return;
        }

        if(mode.currentValue.equals("Deathzone Exp")) {
            if(canClick()) {

                if(tpPackets.isEmpty() && allowTarget) {
                    tpPackets = getPacketsToEntity(selecetedEntity);
                    tpPacketsDeathzone = getPacketsToEntity(selecetedEntity);
                    exemptPackets = getPacketsToEntity(selecetedEntity);
                    allowTarget = false;
                }

                //RenderManager.hitboxes.clear();
                hitEntityDeathzone(selecetedEntity, true);
                //tpPackets.clear();
            }
            return;
        }

        if(canClick()) {

            if(tpPackets.isEmpty()) {
                tpPackets = getPacketsToEntity(selecetedEntity);
            }

            RenderManager.hitboxes.clear();
            hitEntity(selecetedEntity, tpPackets, true);
            tpPackets.clear();
        }

    }

    @Override
    public void onPacket(PacketEvent packetEvent) {

        if(mode.currentValue.equals("Deathzone Exp")) {
            if(packetEvent.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition) {
                C03PacketPlayer.C04PacketPlayerPosition packet = (C03PacketPlayer.C04PacketPlayerPosition) packetEvent.getPacket();

                if(!tpPackets.contains(packet)) {
                    packetEvent.cancelPacket();
                }

            }
        }

    }

    public void getInNearestEntity() {
        for(Entity e : mc.theWorld.loadedEntityList) {
            if(e instanceof EntityBoat || e instanceof EntityMinecart || e instanceof EntityHorse) {

                if(mc.thePlayer.getDistanceToEntity(e) < 5) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C02PacketUseEntity(e, C02PacketUseEntity.Action.INTERACT));
                    return;
                }
            }
        }
        NotificationManager.addChat("No rideable Entity found!");
    }

    public void hitEntity(Entity entity, ArrayList<C03PacketPlayer.C04PacketPlayerPosition> tpPackets, boolean showHitboxes) {

        for(C03PacketPlayer.C04PacketPlayerPosition packet : tpPackets) {
            PacketManager.sendPacketWithoutEvent(packet);
            if(showHitboxes) {
                HitBox hitBox = new HitBox(packet.getPositionX(), packet.getPositionY(), packet.getPositionZ());
                RenderManager.hitboxes.add(hitBox);
            }
        }

        mc.playerController.attackEntity(mc.thePlayer, entity);
        mc.thePlayer.swingItem();
        Collections.reverse(tpPackets);

        for(C03PacketPlayer.C04PacketPlayerPosition packet : tpPackets) {
            PacketManager.sendPacketWithoutEvent(packet);
        }

    }

    public void hitEntityDeathzone(Entity entity, boolean showHitboxes) {

        System.out.println(tpPacketsDeathzone);

        if(!tpPackets.isEmpty()) {
            for(C03PacketPlayer.C04PacketPlayerPosition packet : tpPackets) {
                PacketManager.sendPacket(packet);
                if(showHitboxes) {
                    HitBox hitBox = new HitBox(packet.getPositionX(), packet.getPositionY(), packet.getPositionZ());
                    RenderManager.hitboxes.add(hitBox);
                }
                tpPackets.remove(packet);
                return;
            }
        }

        if(!hitDone){
            RenderManager.hitboxes.clear();
            mc.playerController.attackEntity(mc.thePlayer, entity);
            mc.thePlayer.swingItem();
            Collections.reverse(tpPacketsDeathzone);
            hitDone = true;
        }

        if(!tpPacketsDeathzone.isEmpty()) {
            for(C03PacketPlayer.C04PacketPlayerPosition packet : tpPacketsDeathzone) {
                PacketManager.sendPacket(packet);
                tpPacketsDeathzone.remove(packet);

                if(showHitboxes) {
                    HitBox hitBox = new HitBox(packet.getPositionX(), packet.getPositionY(), packet.getPositionZ());
                    RenderManager.hitboxes.add(hitBox);
                }

                return;
            }
        }

    }

    public Entity getClosestEntity() {
        Entity closestEntity = null;
        double closestDistance = range.cValue;

        for(Entity entity : mc.theWorld.loadedEntityList) {
            if(entity == mc.thePlayer || !(entity instanceof EntityLivingBase) || entity.isDead) continue;
            double distance = getDistanceToTarget(entity);
            if(distance < closestDistance) {
                closestEntity = entity;
                closestDistance = distance;
            }
        }
        return closestEntity;
    }

    public double getDistanceToTarget(Entity entity) {
        double dX = Math.abs(entity.posX - mc.thePlayer.posX);
        double dY = Math.abs(entity.posY - mc.thePlayer.posY);
        double dZ = Math.abs(entity.posZ - mc.thePlayer.posZ);
        return Math.max(dX, Math.max(dY, dZ));
    }

    public boolean canClick() {

        if(mc.thePlayer.ticksExisted % (20 / cps.cValue) == 0) {
            return true;
        }

        return false;
    }

    public ArrayList<C03PacketPlayer.C04PacketPlayerPosition> getPacketsToEntity(Entity entity) {
        ArrayList<C03PacketPlayer.C04PacketPlayerPosition> packets = new ArrayList<>();
        double distance = getDistanceToTarget(entity);

        double packetX = mc.thePlayer.posX;
        double packetY = mc.thePlayer.posY;
        double packetZ = mc.thePlayer.posZ;

        for(int i = 0; i < distance; i++) {

            if(packetX == entity.posX && packetY == entity.posY && packetZ == entity.posZ) {
                break;
            }

            packetX = correctX(packetX, entity.posX);
            packetY = correctY(packetY, entity.posY);
            packetZ = correctZ(packetZ, entity.posZ);

            C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(packetX, packetY, packetZ, false);
            packets.add(packet);

        }
        return packets;
    }

    public int correctX(double playerX, double targetX) {
        int difference = (int) Math.abs(targetX - playerX);
        int step = (difference % 3 == 0) ? 3 : 1;

        if(mode.currentValue.equals("Deathzone Exp")) {
            step = 1;
        }

        if(difference == 0) return (int)playerX;

        int newX = (int) ((targetX > playerX) ? playerX + step : playerX - step);
        return newX;
    }

    public int correctY(double playerY, double targetY) {
        int difference = (int) Math.abs(targetY - playerY);
        int step = (difference % 3 == 0) ? 3 : 1;

        if(difference == 0) return (int) playerY;

        int newY = (int) ((targetY > playerY) ? playerY + step : playerY - step);
        return newY;
    }

    public int correctZ(double playerZ, double targetZ) {
        int difference = (int) Math.abs(targetZ - playerZ);
        int step = (difference % 3 == 0) ? 3 : 1;

        if(mode.currentValue.equals("Deathzone Exp")) {
            step = 1;
        }

        if(difference == 0) return (int) playerZ;

        int newZ = (int) ((targetZ > playerZ) ? playerZ + step : playerZ - step);
        return newZ;
    }

}
