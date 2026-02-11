package peak.managers;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class Rendermanager extends Gui {

    public static void drawRoundedRect(int left, int top, int right, int bottom, int color) {
        Gui.drawRect(left, top, right, bottom, color);
    }

    public static void drawCorner(float centerX, float centerY, float radius, int startAngle) {

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.disableCull();

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        // Wir beginnen einen Triangle Fan.
        // Der erste Punkt ist das Zentrum des Kreises/der Ecke.
        worldrenderer.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);
        worldrenderer.pos(centerX, centerY, 0).endVertex();

        // Nun berechnen wir die Punkte auf dem Bogen
        int segments = 20; // Je höher, desto runder die Ecke
        for (int i = 0; i <= segments; i++) {
            // Berechne den aktuellen Winkel im Bogenmaß (Radians)
            // startAngle ist z.B. 0, 90, 180 oder 270
            double angle = Math.toRadians(startAngle + (i * 90.0 / segments));

            double x = centerX + Math.cos(angle) * radius;
            double y = centerY + Math.sin(angle) * radius;

            worldrenderer.pos(x, y, 0).endVertex();
        }

        tessellator.draw();

        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

}
