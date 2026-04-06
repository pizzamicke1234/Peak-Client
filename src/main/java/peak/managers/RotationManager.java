package peak.managers;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S14PacketEntity;
import net.minecraft.util.MathHelper;
import peak.events.PacketEvent;
import peak.ui.notifications.NotificationManager;

public class RotationManager {

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static float prevServerYaw, prevServerPitch;
    public static float serverYaw = mc.thePlayer.rotationYaw;
    public static float serverPitch = mc.thePlayer.rotationPitch;
    public static boolean overrideRotations = false;

}
