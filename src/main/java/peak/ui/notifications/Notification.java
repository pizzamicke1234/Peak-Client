package peak.ui.notifications;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import peak.managers.ColorManager;
import peak.managers.font.FontUtil;
import peak.managers.render.RenderManager;

import java.awt.*;

public class Notification {

    Minecraft mc = Minecraft.getMinecraft();

    public String title, message;
    public long start, duration;
    public NotificationType type;

    public Notification(String title, String message, NotificationType type, long duration) {
        this.title = title;
        this.message = message;
        this.type = type;
        this.start = System.currentTimeMillis();
        this.duration = duration;
    }

    public void draw(float yOffset) {
        float width = 120;
        float height = 30;
        long elapsed = System.currentTimeMillis() - start;

        float animation = getAnimationFactor(elapsed);

        float x = mc.displayWidth  / mc.gameSettings.guiScale - (width + 5) * animation;
        float y = mc.displayHeight / mc.gameSettings.guiScale - (height + 5) - yOffset;

        Color Rectcolor = new Color(20, 20, 20, 180);
        RenderManager.drawRoundedRect(x, y, width, height, 6.0f, Rectcolor);

        float progress = 1.0f - ((float) elapsed / duration);
        Color barColor = new Color(0, 170, 255);
        RenderManager.drawRoundedRect(x, y + height - 2, width * progress, 2, 1.0f, barColor);

        FontUtil.smaller.drawCenteredString(title, (int)x + 60, (int)y + 3, -1);
        FontUtil.smallest.drawString(message, (int)x + 4, (int)y + 16, -1);
    }

    private float getAnimationFactor(long elapsed) {
        if (elapsed < 300) return elapsed / 300f;
        if (elapsed > duration - 300) return Math.max(0, (duration - elapsed) / 300f);
        return 1.0f;
    }

    public boolean isFinished() {
        return System.currentTimeMillis() - start > duration;
    }

    public enum NotificationType {
        INFO,
        WARNING,
        MODULE;
    }

}
