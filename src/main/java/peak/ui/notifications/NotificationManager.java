package peak.ui.notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager {

    public static Minecraft mc = Minecraft.getMinecraft();
    public static CopyOnWriteArrayList<Notification> notifications = new CopyOnWriteArrayList<>();

    public static void addChat(String message) {
        if (mc.thePlayer != null) {
            mc.thePlayer.addChatMessage(new ChatComponentText("§8[§bPeak§8] §f" + message));
        }
    }

    public static void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public static void removeNotification(Notification notification) {
        if(notifications.contains(notification)) {
            notifications.remove(notification);
        }
    }

    public static void render() {
        float yOffset = 0;
        for (Notification n : notifications) {
            if (n.isFinished()) {
                notifications.remove(n);
                continue;
            }
            n.draw(yOffset);
            yOffset += 40;
        }
    }

}
