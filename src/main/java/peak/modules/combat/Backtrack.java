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

import static peak.managers.render.RenderManager.drawSelectionBox;

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
        double serverX, serverY, serverZ;
        boolean hasServerPosition = false;
    }

    private static Field s14Field;
    private static Field s18Field;
    private static Field c02Field;

    // === Delta fields for S14 inner classes (this is what was failing before) ===
    private static Field s15DeltaXField;
    private static Field s15DeltaYField;
    private static Field s15DeltaZField;
    private static Field s17DeltaXField;
    private static Field s17DeltaYField;
    private static Field s17DeltaZField;

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

        // S15PacketEntityRelMove deltas
        try {
            s15DeltaXField = S14PacketEntity.S15PacketEntityRelMove.class.getDeclaredField("field_149073_e");
            s15DeltaXField.setAccessible(true);
        } catch (Exception e) {
            try { s15DeltaXField = S14PacketEntity.S15PacketEntityRelMove.class.getDeclaredField("e"); s15DeltaXField.setAccessible(true); } catch (Exception ignored) {}
        }
        try {
            s15DeltaYField = S14PacketEntity.S15PacketEntityRelMove.class.getDeclaredField("field_149072_f");
            s15DeltaYField.setAccessible(true);
        } catch (Exception e) {
            try { s15DeltaYField = S14PacketEntity.S15PacketEntityRelMove.class.getDeclaredField("f"); s15DeltaYField.setAccessible(true); } catch (Exception ignored) {}
        }
        try {
            s15DeltaZField = S14PacketEntity.S15PacketEntityRelMove.class.getDeclaredField("field_149075_g");
            s15DeltaZField.setAccessible(true);
        } catch (Exception e) {
            try { s15DeltaZField = S14PacketEntity.S15PacketEntityRelMove.class.getDeclaredField("g"); s15DeltaZField.setAccessible(true); } catch (Exception ignored) {}
        }

        // S17PacketEntityLookMove deltas (same field names)
        try {
            s17DeltaXField = S14PacketEntity.S17PacketEntityLookMove.class.getDeclaredField("field_149073_e");
            s17DeltaXField.setAccessible(true);
        } catch (Exception e) {
            try { s17DeltaXField = S14PacketEntity.S17PacketEntityLookMove.class.getDeclaredField("e"); s17DeltaXField.setAccessible(true); } catch (Exception ignored) {}
        }
        try {
            s17DeltaYField = S14PacketEntity.S17PacketEntityLookMove.class.getDeclaredField("field_149072_f");
            s17DeltaYField.setAccessible(true);
        } catch (Exception e) {
            try { s17DeltaYField = S14PacketEntity.S17PacketEntityLookMove.class.getDeclaredField("f"); s17DeltaYField.setAccessible(true); } catch (Exception ignored) {}
        }
        try {
            s17DeltaZField = S14PacketEntity.S17PacketEntityLookMove.class.getDeclaredField("field_149075_g");
            s17DeltaZField.setAccessible(true);
        } catch (Exception e) {
            try { s17DeltaZField = S14PacketEntity.S17PacketEntityLookMove.class.getDeclaredField("g"); s17DeltaZField.setAccessible(true); } catch (Exception ignored) {}
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

        Entity entity = mc.theWorld.getEntityByID(entityId);
        if (entity == null) return;

        // Initial position (so box always appears)
        if (!queue.hasServerPosition) {
            queue.serverX = entity.posX;
            queue.serverY = entity.posY;
            queue.serverZ = entity.posZ;
            queue.hasServerPosition = true;
        }

        // === EXTRACT EXACT SERVER POSITION FROM CANCELLED PACKET ===
        if (packet instanceof S18PacketEntityTeleport) {
            try {
                Field xField = null, yField = null, zField = null;
                try {
                    xField = S18PacketEntityTeleport.class.getDeclaredField("field_149449_b");
                    yField = S18PacketEntityTeleport.class.getDeclaredField("field_149450_c");
                    zField = S18PacketEntityTeleport.class.getDeclaredField("field_149447_d");
                } catch (Exception e) {
                    try {
                        xField = S18PacketEntityTeleport.class.getDeclaredField("b");
                        yField = S18PacketEntityTeleport.class.getDeclaredField("c");
                        zField = S18PacketEntityTeleport.class.getDeclaredField("d");
                    } catch (Exception ignored) {}
                }
                if (xField != null) {
                    xField.setAccessible(true); yField.setAccessible(true); zField.setAccessible(true);
                    queue.serverX = ((Integer) xField.get(packet)) / 32.0D;
                    queue.serverY = ((Integer) yField.get(packet)) / 32.0D;
                    queue.serverZ = ((Integer) zField.get(packet)) / 32.0D;
                }
            } catch (Exception ignored) {}
        }
        else if (packet instanceof S14PacketEntity.S15PacketEntityRelMove) {
            try {
                if (s15DeltaXField != null) {
                    byte dx = (Byte) s15DeltaXField.get(packet);
                    byte dy = (Byte) s15DeltaYField.get(packet);
                    byte dz = (Byte) s15DeltaZField.get(packet);
                    queue.serverX = entity.posX + (dx / 32.0D);
                    queue.serverY = entity.posY + (dy / 32.0D);
                    queue.serverZ = entity.posZ + (dz / 32.0D);
                }
            } catch (Exception ignored) {}
        }
        else if (packet instanceof S14PacketEntity.S17PacketEntityLookMove) {
            try {
                if (s17DeltaXField != null) {
                    byte dx = (Byte) s17DeltaXField.get(packet);
                    byte dy = (Byte) s17DeltaYField.get(packet);
                    byte dz = (Byte) s17DeltaZField.get(packet);
                    queue.serverX = entity.posX + (dx / 32.0D);
                    queue.serverY = entity.posY + (dy / 32.0D);
                    queue.serverZ = entity.posZ + (dz / 32.0D);
                }
            } catch (Exception ignored) {}
        }

        queue.packets.offer(new TimedPacket(packet, System.currentTimeMillis()));

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
        }
    }

    private void flushAllPackets() {
        for (Integer entityId : new ArrayList<>(entityQueues.keySet())) flushEntityPackets(entityId);
    }

    private void flushEntityPackets(int entityId) {
        entityQueues.remove(entityId);
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
            Entity entity = mc.theWorld.getEntityByID(entry.getKey());

            if (entity == null || !shouldBacktrackEntity(entity)) continue;

            // GREEN = server position from cancelled packets (should now move correctly)
            if (queue.hasServerPosition) {
                drawSelectionBox(
                        queue.serverX,
                        queue.serverY,
                        queue.serverZ,
                        entity.width,
                        entity.height,
                        new Color(56, 230, 48, 150)
                );
            }

            // RED = client position (for comparison)
            drawSelectionBox(
                    entity.posX,
                    entity.posY,
                    entity.posZ,
                    entity.width,
                    entity.height,
                    new Color(230, 48, 48, 150)
            );
        }
    }
}