package peak.ui.mainmenus.elements;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import peak.managers.font.FontUtil;

public class PeakButton extends Gui {

    public int buttonid, left, top, right, bottom;
    public String buttonText;
    public boolean hovered;

    public PeakButton(int buttonid, int left, int top, int right, int bottom, String buttonText) {
        this.buttonid = buttonid;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.buttonText = buttonText;
    }

    public void drawButton(int mouseX, int mouseY) {

        hovered = mouseX >= left && mouseY >= top && mouseX <= right && mouseY <= bottom;

        int startcolor;
        int endcolor;

        int outlinecolor;

        if (hovered) {
            startcolor = 0xFF6A0DAD;
            endcolor = 0xFF9932CC;
            outlinecolor = 0xCC000000;
        }else{
            startcolor = 0xFF2D004B;
            endcolor = 0xFF4B0082;
            outlinecolor = 0xFF00ADFF;
        }

        drawHorizontalGradientRect(this.left, this.top, this.right, this.bottom, startcolor, endcolor);

        FontUtil.normal.drawCenteredString(this.buttonText, this.left + (this.right - this.left) / 2, this.top + (this.bottom - this.top) / 2 - 6, -1);

        drawOutline(left, top, right, bottom, 2, 0xFF0040FF);


    }

    public void drawOutline(int left, int top, int right, int bottom, int thickness, int color) {
        this.drawRect(left - thickness, top - thickness, right + thickness, top, color);
        this.drawRect(left - thickness, bottom, right + thickness, bottom + thickness, color);
        this.drawRect(left - thickness, top, left, bottom, color);
        this.drawRect(right, top, right + thickness, bottom, color);
    }

    public void drawHorizontalGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        float f = (float)(startColor >> 24 & 255) / 255.0F;
        float f1 = (float)(startColor >> 16 & 255) / 255.0F;
        float f2 = (float)(startColor >> 8 & 255) / 255.0F;
        float f3 = (float)(startColor & 255) / 255.0F;

        float f4 = (float)(endColor >> 24 & 255) / 255.0F;
        float f5 = (float)(endColor >> 16 & 255) / 255.0F;
        float f6 = (float)(endColor >> 8 & 255) / 255.0F;
        float f7 = (float)(endColor & 255) / 255.0F;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);

        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();

        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);

        worldrenderer.pos((double)left, (double)top, 0.0D).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos((double)left, (double)bottom, 0.0D).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos((double)right, (double)bottom, 0.0D).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos((double)right, (double)top, 0.0D).color(f5, f6, f7, f4).endVertex();

        tessellator.draw();

        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

}
