package peak.ui.mainmenus.elements;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import peak.managers.font.FontUtil;
import peak.managers.render.RenderManager;

import java.awt.*;

public class PeakButton extends Gui {

    public int buttonID, x, y, width, height;
    public String buttonText;
    public boolean hovered;

    public PeakButton(int buttonID, int x, int y, int width, int height, String buttonText) {
        this.buttonID = buttonID;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buttonText = buttonText;
    }

    public boolean isClicked(int mouseX, int mouseY) {
        return mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);
    }

    public void drawButton(int mouseX, int mouseY) {

        hovered = mouseX >= x && mouseY >= y && mouseX <= (x + width) && mouseY <= (y + height);

        Color normalColor = new Color(25, 25, 25, 220);
        Color hoverColor = new Color(80, 80, 80, 180);

        Color buttonColor = hovered ? hoverColor : normalColor;

        //drawHorizontalGradientRect(this.left, this.top, this.right, this.bottom, startcolor, endcolor);
        RenderManager.drawRoundedRect(this.x, this.y, this.width, this.height, 5, buttonColor);

        FontUtil.normal.drawCenteredString(this.buttonText, this.x + this.width / 2, this.y + this.height / 2 - 4, -1);

        //drawOutline(x, y, width, height, 2, 0xFF0040FF);


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
