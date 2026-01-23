package peak.managers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import peak.Client;
import peak.modules.misc.Disabler;

import java.util.ArrayList;
import java.util.List;

public class PacketManager {

    public static Minecraft mc = Minecraft.getMinecraft();

    private static final List<Class<? extends Packet>> canceledTypes = new ArrayList<>();
    public static Disabler disabler = (Disabler) Client.getModulebyName("Disabler");

    public static boolean cancelTransactions = false;
    public static final List<Packet<?>> blinkBuffer = new ArrayList<>();

    public static void sendPacket(Packet<?> packet) {
        mc.thePlayer.sendQueue.addToSendQueue(packet);
        if(disabler.toggled && disabler.debug.isTrue()) {
            NotificationManager.addChat("Packet send | " + packet);
        }
    }

    public static void releasePackets() {
        if (blinkBuffer.isEmpty()) return;

        try {
            if (mc.getNetHandler() != null && mc.getNetHandler().getNetworkManager() != null) {
                for (Packet<?> packet : blinkBuffer) {
                    mc.getNetHandler().getNetworkManager().dispatchPacket(packet, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        blinkBuffer.clear();
    }

    public static boolean isCheckPacket(Packet<?> packet) {
        return packet instanceof C0FPacketConfirmTransaction || packet instanceof C00PacketKeepAlive;
    }

    public static void cancelPacketType(Class<? extends Packet> packetClass) {
        if (!canceledTypes.contains(packetClass)) {
            canceledTypes.add(packetClass);
        }
    }

    public static void uncancelPacketType(Class<? extends Packet> packetClass) {
        canceledTypes.remove(packetClass);
    }

    public static boolean shouldCancel(Packet<?> packet) {
        for (Class<? extends Packet> type : canceledTypes) {
            if (type.isInstance(packet)) {
                return true;
            }
            if (cancelTransactions && packet instanceof net.minecraft.network.play.client.C0FPacketConfirmTransaction) {
                return true;
            }
        }
        return false;
    }

    public static void clearCanceledTypes() {
        canceledTypes.clear();
    }

}
