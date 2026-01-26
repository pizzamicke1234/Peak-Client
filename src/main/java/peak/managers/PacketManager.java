package peak.managers;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import peak.Client;
import peak.modules.combat.Velocity;
import peak.modules.misc.Disabler;

import java.util.ArrayList;
import java.util.List;

public class PacketManager {

    public static Minecraft mc = Minecraft.getMinecraft();

    private static final List<Class<? extends Packet>> canceledTypes = new ArrayList<>();
    public static Packet allowedPacket;

    public static Disabler disabler = (Disabler) Client.getModulebyName("Disabler");

    private static boolean velotransaction = false;

    public static void sendPacket(Packet<?> packet) {
        allowedPacket = packet;
        mc.thePlayer.sendQueue.addToSendQueue(packet);
        if(disabler.toggled && disabler.debug.isTrue()) {
            NotificationManager.addChat("Packet send | " + packet);
        }
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
        }
        return false;
    }

    public static void clearCanceledTypes() {
        canceledTypes.clear();
    }

    public static boolean handlePacketReceive(Packet<?> packet) {
        Velocity velocity = (Velocity) Client.getModulebyName("Velocity");

        if (packet instanceof S12PacketEntityVelocity) {
            S12PacketEntityVelocity s12 = (S12PacketEntityVelocity) packet;

            if (mc.thePlayer != null && s12.getEntityID() == mc.thePlayer.getEntityId()) {
                if (velocity.toggled) {

                    switch (velocity.velocityMode.current_value) {

                        case "Vanilla":
                            return true;

                        case "Vulcan":
                            cancelPacketType(C0FPacketConfirmTransaction.class);
                            int p;
                            short p1;
                            if(velotransaction) p = 1;
                            else p = -1;
                            p1 = (short) (p * -1);
                            //uncancelPacketType(C0FPacketConfirmTransaction.class);
                            sendPacket(new C0FPacketConfirmTransaction(p, p1, true));
                            velotransaction = !velotransaction;
                            uncancelPacketType(C0FPacketConfirmTransaction.class);
                            return true;
                    }
                }
            }
        }
        return false;
    }

}
