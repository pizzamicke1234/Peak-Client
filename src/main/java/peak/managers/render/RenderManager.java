package peak.managers.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.Color;

public class RenderManager {

    private static final ShaderManager ROUNDED_SHADER = new ShaderManager("assets/minecraft/peak/render/shaders/rounded.frag");

    public static void drawRoundedRect(float x, float y, float width, float height, float radius, Color color) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableAlpha(); // Wichtig, damit der Shader die Kanten glätten kann

        ROUNDED_SHADER.use();

        ROUNDED_SHADER.setUniform("location", x * sr.getScaleFactor(),
                (Minecraft.getMinecraft().displayHeight - (y + height) * sr.getScaleFactor()));
        ROUNDED_SHADER.setUniform("rectSize", width * sr.getScaleFactor(), height * sr.getScaleFactor());
        ROUNDED_SHADER.setUniform("radius", radius * sr.getScaleFactor());

        ROUNDED_SHADER.setUniform("color", color.getRed() / 255f, color.getGreen() / 255f,
                color.getBlue() / 255f, color.getAlpha() / 255f);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x, y + height);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();

        ROUNDED_SHADER.stop();

        GlStateManager.enableAlpha();
        GlStateManager.disableBlend();
    }
}