package peak.managers.render;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class RenderManager {

    private static Minecraft mc = Minecraft.getMinecraft();
    public static CopyOnWriteArrayList<HitBox> hitboxes = new CopyOnWriteArrayList<>();
    private static final ShaderManager ROUNDED_SHADER = new ShaderManager("assets/minecraft/peak/render/shaders/rounded.frag");

    private static final Map<UUID, ResourceLocation> downloadedSkins = new HashMap<>();

    public static void drawRoundedRect(float x, float y, float width, float height, float radius, Color color) {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableAlpha();

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

    public static void drawPlayerHead(ResourceLocation skin, int x, int y, int size) {
        GlStateManager.pushMatrix();
        GlStateManager.color(255, 255, 255);

        if(skin == null) {
            skin = DefaultPlayerSkin.getDefaultSkinLegacy();
        }

        Minecraft.getMinecraft().getTextureManager().bindTexture(skin);

        Gui.drawScaledCustomSizeModalRect(x, y, 8.0F, 8.0F, 8, 8, size, size, 64.0F, 64.0F);
        Gui.drawScaledCustomSizeModalRect(x, y, 40.0F, 8.0F, 8, 8, size, size, 64.0F, 64.0F);

        GlStateManager.popMatrix();
    }

    public static ResourceLocation getSkinOrDownload(final GameProfile profile) {
        if (profile == null) return DefaultPlayerSkin.getDefaultSkinLegacy();

        if (downloadedSkins.containsKey(profile.getId())) {
            return downloadedSkins.get(profile.getId());
        }

        Minecraft.getMinecraft().getSkinManager().loadProfileTextures(profile, (type, location, tex) -> {
            if (type == MinecraftProfileTexture.Type.SKIN) {
                downloadedSkins.put(profile.getId(), location);
            }
        }, true);

        return DefaultPlayerSkin.getDefaultSkinLegacy();
    }

    public static ResourceLocation getSessionSkin(Session session) {
        GameProfile gameProfile = session.getProfile();
        return getSkinOrDownload(gameProfile);
    }

    public static void drawEntityESP(Entity entity, float partialTicks, Color color) {

        net.minecraft.client.renderer.entity.RenderManager rm = mc.getRenderManager();

        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;

        AxisAlignedBB entityBox = entity.getEntityBoundingBox();

        double rX = x - rm.renderPosX;
        double rY = y - rm.renderPosY;
        double rZ = z - rm.renderPosZ;

        AxisAlignedBB renderBox = new AxisAlignedBB(
                entityBox.minX - entity.posX + rX,
                entityBox.minY - entity.posY + rY,
                entityBox.minZ - entity.posZ + rZ,
                entityBox.maxX - entity.posX + rX,
                entityBox.maxY - entity.posY + rY,
                entityBox.maxZ - entity.posZ + rZ
        );

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.depthMask(false);
        GL11.glLineWidth(2.0F);

        RenderGlobal.drawOutlinedBoundingBox(
                renderBox,
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                color.getAlpha()
        );

        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

    }

    public static void drawMark(Entity entity, float partialTicks, Color color) {

        net.minecraft.client.renderer.entity.RenderManager rm = mc.getRenderManager();

        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;

        AxisAlignedBB entityBox = entity.getEntityBoundingBox();

        double rX = x - rm.renderPosX;
        double rY = y - rm.renderPosY;
        double rZ = z - rm.renderPosZ;

        double x1 = entityBox.minX - entity.posX + rX - 0.1D;
        double x2 = entityBox.maxX - entity.posX + rX + 0.1D;
        double y1 = entityBox.maxY - entity.posY + rY + 0.2D;
        double y2 = y1 + 0.2D;
        double z1 = entityBox.minZ - entity.posZ + rZ - 0.1D;
        double z2 = entityBox.maxZ - entity.posZ + rZ + 0.1D;

        AxisAlignedBB renderBox = new AxisAlignedBB(x1, y1, z1, x2, y2, z2);

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.disableTexture2D();
        GlStateManager.disableDepth();
        GlStateManager.disableCull();
        GlStateManager.depthMask(false);
        GL11.glLineWidth(2.0F);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        drawFilledBoundingBox(renderBox, color);

        /*RenderGlobal.drawOutlinedBoundingBox(
                renderBox,
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                color.getAlpha()
        );*/

        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

    }

    /** Holy vibe coded method
     * Total wasted tries to make this shit work: 4
     */
    public static void drawFilledBoundingBox(AxisAlignedBB mask, Color color) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        // These need to be ints (0-255) for the POSITION_COLOR format in 1.8.9
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int a = color.getAlpha();

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);

        // Bottom
        worldrenderer.pos(mask.minX, mask.minY, mask.minZ).color(r, g, b, a).endVertex();
        worldrenderer.pos(mask.maxX, mask.minY, mask.minZ).color(r, g, b, a).endVertex();
        worldrenderer.pos(mask.maxX, mask.minY, mask.maxZ).color(r, g, b, a).endVertex();
        worldrenderer.pos(mask.minX, mask.minY, mask.maxZ).color(r, g, b, a).endVertex();

        // Top
        worldrenderer.pos(mask.minX, mask.maxY, mask.minZ).color(r, g, b, a).endVertex();
        worldrenderer.pos(mask.minX, mask.maxY, mask.maxZ).color(r, g, b, a).endVertex();
        worldrenderer.pos(mask.maxX, mask.maxY, mask.maxZ).color(r, g, b, a).endVertex();
        worldrenderer.pos(mask.maxX, mask.maxY, mask.minZ).color(r, g, b, a).endVertex();

        // North
        worldrenderer.pos(mask.minX, mask.minY, mask.minZ).color(r, g, b, a).endVertex();
        worldrenderer.pos(mask.minX, mask.maxY, mask.minZ).color(r, g, b, a).endVertex();
        worldrenderer.pos(mask.maxX, mask.maxY, mask.minZ).color(r, g, b, a).endVertex();
        worldrenderer.pos(mask.maxX, mask.minY, mask.minZ).color(r, g, b, a).endVertex();

        // South
        worldrenderer.pos(mask.maxX, mask.minY, mask.maxZ).color(r, g, b, a).endVertex();
        worldrenderer.pos(mask.maxX, mask.maxY, mask.maxZ).color(r, g, b, a).endVertex();
        worldrenderer.pos(mask.minX, mask.maxY, mask.maxZ).color(r, g, b, a).endVertex();
        worldrenderer.pos(mask.minX, mask.minY, mask.maxZ).color(r, g, b, a).endVertex();

        // West
        worldrenderer.pos(mask.minX, mask.minY, mask.maxZ).color(r, g, b, a).endVertex();
        worldrenderer.pos(mask.minX, mask.maxY, mask.maxZ).color(r, g, b, a).endVertex();
        worldrenderer.pos(mask.minX, mask.maxY, mask.minZ).color(r, g, b, a).endVertex();
        worldrenderer.pos(mask.minX, mask.minY, mask.minZ).color(r, g, b, a).endVertex();

        // East
        worldrenderer.pos(mask.maxX, mask.minY, mask.minZ).color(r, g, b, a).endVertex();
        worldrenderer.pos(mask.maxX, mask.maxY, mask.minZ).color(r, g, b, a).endVertex();
        worldrenderer.pos(mask.maxX, mask.maxY, mask.maxZ).color(r, g, b, a).endVertex();
        worldrenderer.pos(mask.maxX, mask.minY, mask.maxZ).color(r, g, b, a).endVertex();

        tessellator.draw();
    }

}