package peak.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.network.play.client.C02PacketUseEntity;
import peak.modules.Module;
import peak.modules.settings.BoolSetting;
import peak.modules.settings.NumberSetting;
import peak.managers.PacketManager;
import peak.managers.render.RenderManager;
import peak.events.PacketEvent;
import peak.events.TickEvent;
import peak.events.AttackEvent;
import peak.events.RenderEvent;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.*;

public class Backtrack extends Module {

    public final NumberSetting delaySetting = new NumberSetting("DelayMS", true, 30.0, 500.0, 100.0, 10.0);
    public final NumberSetting rangeSetting = new NumberSetting("Range", true, 3.0, 8.0, 6.0, 0.5);
    public final BoolSetting playersOnly = new BoolSetting("PlayersOnly", true, true);
    public final BoolSetting smartRelease = new BoolSetting("SmartRelease", true, true);
    public final BoolSetting esp = new BoolSetting("ESP", true, true);

    private final Map<Integer, EntityQueue> entityQueues = new HashMap<>();
    private static final int MAX_QUEUE_SIZE = 25;

    private static class TimedPacket {
        final Packet<?> packet;
        final long timestamp;
        TimedPacket(Packet<?> packet, long timestamp) { this.packet = packet; this.timestamp = timestamp; }
    }

    private static class EntityQueue {
        final Queue<TimedPacket> packets = new LinkedList<>();
        long lastRelease = 0;
        double storedX, storedY, storedZ;
        boolean hasPosition = false;
    }

    private static Field s14Field;
    private static Field s18Field;
    private static Field c02Field;
    private static boolean reflectionDone = false;

    public Backtrack() {
        super("Backtrack", 0, Category.COMBAT, true);
        addSetting(delaySetting, rangeSetting, playersOnly, smartRelease, esp);
        initReflection();
    }

    private void initReflection() {
        if (reflectionDone) return;
        try {
            s14Field = S14PacketEntity.class.getDeclaredField("entityId");
            s14Field.setAccessible(true);
        } catch (Exception e) {
            try { s14Field = S14PacketEntity.class.getDeclaredField("field_149074_a"); s14Field.setAccessible(true); } catch (Exception ignored) {}
        }
        try {
            s18Field = S18PacketEntityTeleport.class.getDeclaredField("entityId");
            s18Field.setAccessible(true);
        } catch (Exception e) {
            try { s18Field = S18PacketEntityTeleport.class.getDeclaredField("field_149451_a"); s18Field.setAccessible(true); } catch (Exception ignored) {}
        }
        try {
            c02Field = C02PacketUseEntity.class.getDeclaredField("entityId");
            c02Field.setAccessible(true);
        } catch (Exception e) {
            try { c02Field = C02PacketUseEntity.class.getDeclaredField("field_149562_a"); c02Field.setAccessible(true); } catch (Exception ignored) {}
        }
        reflectionDone = true;
    }

    @Override
    public void onDisable() { flushAllPackets(); }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if (tickType == TickEvent.TickType.POST) releaseDuePackets();
    }

    @Override
    public void onPacket(PacketEvent event) {
        if (!toggled || event.getType() != PacketEvent.Type.RECEIVE) return;
        Packet<?> packet = event.getPacket();
        if (packet == null) return;
        if (packet instanceof S14PacketEntity || packet instanceof S18PacketEntityTeleport) {
            int entityId = getEntityId(packet);
            if (entityId == -1 || entityId == mc.thePlayer.getEntityId()) return;
            Entity entity = mc.theWorld.getEntityByID(entityId);
            if (!shouldBacktrackEntity(entity)) return;
            queuePacket(entityId, packet);
            event.cancelPacket();
        }
    }

    @Override
    public void onAttack(AttackEvent event) {
        if (!toggled || !smartRelease.status) return;
        C02PacketUseEntity packet = event.getAttackPacket();
        if (packet == null) return;
        int entityId = getEntityIdFromUseEntity(packet);
        if (entityId != -1) flushEntityPackets(entityId);
    }

    @Override
    public void onRender(RenderEvent event) {
        if (!toggled || !esp.status) return;
        renderBacktrackTargets(event.getPartialTicks());
    }

    private boolean shouldBacktrackEntity(Entity entity) {
        if (entity == null || entity.isDead) return false;
        if (playersOnly.status && !(entity instanceof EntityPlayer)) return false;
        return mc.thePlayer.getDistanceToEntity(entity) <= rangeSetting.cValue;
    }

    private void queuePacket(int entityId, Packet<?> packet) {
        EntityQueue queue = entityQueues.computeIfAbsent(entityId, k -> new EntityQueue());
        queue.packets.offer(new TimedPacket(packet, System.currentTimeMillis()));

        // Store the entity's current position for ESP rendering
        Entity entity = mc.theWorld.getEntityByID(entityId);
        if (entity != null) {
            queue.storedX = entity.posX;
            queue.storedY = entity.posY;
            queue.storedZ = entity.posZ;
            queue.hasPosition = true;
        }

        if (queue.packets.size() > MAX_QUEUE_SIZE) {
            TimedPacket oldest = queue.packets.poll();
            if (oldest != null) PacketManager.receivePacketWithoutEvent(oldest.packet);
        }
    }

    private void releaseDuePackets() {
        if (entityQueues.isEmpty()) return;
        long now = System.currentTimeMillis();
        long delayMs = (long) delaySetting.cValue;
        Iterator<Map.Entry<Integer, EntityQueue>> it = entityQueues.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, EntityQueue> entry = it.next();
            EntityQueue queue = entry.getValue();
            if (now - queue.lastRelease < 16) continue;
            TimedPacket tp = queue.packets.peek();
            if (tp != null && now - tp.timestamp >= delayMs) {
                queue.packets.poll();
                PacketManager.receivePacketWithoutEvent(tp.packet);
                queue.lastRelease = now;
            }
            if (queue.packets.isEmpty()) it.remove();
        }
    }

    private void flushAllPackets() {
        for (Integer entityId : new ArrayList<>(entityQueues.keySet())) flushEntityPackets(entityId);
    }

    private void flushEntityPackets(int entityId) {
        EntityQueue queue = entityQueues.remove(entityId);
        if (queue != null) for (TimedPacket tp : queue.packets) PacketManager.receivePacketWithoutEvent(tp.packet);
    }

    private int getEntityId(Packet<?> packet) {
        try {
            if (packet instanceof S14PacketEntity && s14Field != null) return (int) s14Field.get(packet);
            if (packet instanceof S18PacketEntityTeleport && s18Field != null) return (int) s18Field.get(packet);
        } catch (Exception ignored) {}
        return -1;
    }

    private int getEntityIdFromUseEntity(C02PacketUseEntity packet) {
        try {
            if (c02Field != null) return (int) c02Field.get(packet);
        } catch (Exception ignored) {}
        return -1;
    }

    private void renderBacktrackTargets(float partialTicks) {
        for (Map.Entry<Integer, EntityQueue> entry : entityQueues.entrySet()) {
            EntityQueue queue = entry.getValue();
            if (queue.hasPosition && !queue.packets.isEmpty()) {
                // Create a dummy entity at the stored position for ESP rendering
                Entity entity = mc.theWorld.getEntityByID(entry.getKey());
                if (entity != null && shouldBacktrackEntity(entity)) {
                    // Temporarily set entity position to stored backtrack position
                    double originalX = entity.posX;
                    double originalY = entity.posY;
                    double originalZ = entity.posZ;

                    entity.posX = queue.storedX;
                    entity.posY = queue.storedY;
                    entity.posZ = queue.storedZ;

                    // Draw ESP at backtrack position
                    Color espColor = new Color( 56 , 230 , 48 , 150);
                    RenderManager.drawEntityESP(entity, partialTicks, espColor);

                    // Restore original position
                    entity.posX = originalX;
                    entity.posY = originalY;
                    entity.posZ = originalZ;
                }
            }
        }
    }

}
