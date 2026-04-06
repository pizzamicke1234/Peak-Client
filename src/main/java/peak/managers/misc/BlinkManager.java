package peak.managers.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import peak.events.PacketEvent;
import peak.managers.PacketManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BlinkManager {

    public static final ConcurrentLinkedQueue<Packet<?>> packets = new ConcurrentLinkedQueue<>();
    public static boolean blinking, dispatch, accepted;
    public static ArrayList<Class<?>> exemptedPackets = new ArrayList<>();
    private static Minecraft mc = Minecraft.getMinecraft();

    public final void onPacketSend(PacketEvent packetEvent) {

        if (mc.thePlayer == null) {
            packets.clear();
            exemptedPackets.clear();
            return;
        }

        if(mc.thePlayer.ticksExisted < 1 && !accepted) {
            packets.clear();
            blinking = false;
            accepted = true;
        }

        if (mc.thePlayer.isDead || mc.isSingleplayer() || !mc.getNetHandler().doneLoadingTerrain) {
            packets.forEach(PacketManager::sendPacketWithoutEvent);
            packets.clear();
            blinking = false;
            exemptedPackets.clear();
            return;
        }

        final Packet packet = packetEvent.getPacket();
        if (!packet.getClass().getName().contains(".client.")) {
            return;
        }

        if (packet instanceof C00Handshake || packet instanceof C00PacketLoginStart ||
                packet instanceof C00PacketServerQuery || packet instanceof C01PacketPing ||
                packet instanceof C01PacketEncryptionResponse) {
            return;
        }

        if (blinking && !dispatch) {

            PingSpoofManager.spoofing = false;

            if (!packetEvent.isCanceled()) {
                packets.add(packet);
                packetEvent.cancelPacket();
            }
        } else if (packet instanceof C03PacketPlayer) {
            packets.forEach(PacketManager::sendPacketWithoutEvent);
            packets.clear();
            dispatch = false;
        }
    }

    public static void dispatch() {
        dispatch = true;
    }

}
