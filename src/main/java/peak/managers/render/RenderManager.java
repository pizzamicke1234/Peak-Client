package peak.managers.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.concurrent.CopyOnWriteArrayList;

public class RenderManager {

    Minecraft mc = Minecraft.getMinecraft();
    public static CopyOnWriteArrayList<HitBox> hitboxes = new CopyOnWriteArrayList<>();
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

    public static void drawHitboxes(){
        if(hitboxes.size() > 0) {
            for(HitBox hitBox : hitboxes) {
                drawHitboxAt(hitBox.x, hitBox.y, hitBox.z);
            }
        }
    }

    public static void drawHitboxAt(double x, double y, double z) {
        net.minecraft.client.renderer.entity.RenderManager rm = Minecraft.getMinecraft().getRenderManager();

        double renderX = x - rm.viewerPosX;
        double renderY = y - rm.viewerPosY;
        double renderZ = z - rm.viewerPosZ;

        float w = 0.3F;
        float h = 1.8F;

        AxisAlignedBB bb = new AxisAlignedBB(
                renderX - w, renderY, renderZ - w,
                renderX + w, renderY + h, renderZ + w
        );

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);

        GL11.glLineWidth(2.0F);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        RenderGlobal.drawSelectionBoundingBox(bb);

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawPlayerHead(AbstractClientPlayer player, int x, int y, int size) {
        GlStateManager.pushMatrix();
        GlStateManager.color(255, 255, 255);

        ResourceLocation skin = player.getLocationSkin();
        Minecraft.getMinecraft().getTextureManager().bindTexture(skin);

        Gui.drawScaledCustomSizeModalRect(x, y, 8.0F, 8.0F, 8, 8, size, size, 64.0F, 64.0F);
        Gui.drawScaledCustomSizeModalRect(x, y, 40.0F, 8.0F, 8, 8, size, size, 64.0F, 64.0F);

        GlStateManager.popMatrix();
    }

}