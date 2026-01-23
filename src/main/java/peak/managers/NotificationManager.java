package peak.managers;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class NotificationManager {

    public static Minecraft mc = Minecraft.getMinecraft();

    public static void addChat(String message) {
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatMessage(new ChatComponentText("§8[§bPeak§8] §f" + message));
        }
    }

}
