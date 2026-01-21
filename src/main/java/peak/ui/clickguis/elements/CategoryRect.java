package peak.ui.clickguis.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import peak.managers.font.FontUtil;


public class CategoryRect {

    public Minecraft mc = Minecraft.getMinecraft();

    public String name;
    public int left, top, right, bottom, color;

    private boolean dragging;
    final boolean hasText;
    private int offsetL, offsetT, offsetR, offsetB;

    public CategoryRect(String name, int left, int top, int right, int bottom, int color, boolean hasText) {
        this.name = name;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.color = color;
        this.hasText = hasText;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isHovered(mouseX, mouseY)) {
            this.dragging = true;
            // Wir speichern, wie weit die Maus von jeder Kante entfernt ist
            this.offsetL = this.left - mouseX;
            this.offsetT = this.top - mouseY;
            this.offsetR = this.right - mouseX;
            this.offsetB = this.bottom - mouseY;
        }
    }

    private boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= this.left && mouseX <= this.right &&
                mouseY >= this.top && mouseY <= this.bottom;
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        this.dragging = false;
    }

    public void draw(int mouseX, int mouseY) {
        if (this.dragging) {
            this.left = mouseX + offsetL;
            this.top = mouseY + offsetT;
            this.right = mouseX + offsetR;
            this.bottom = mouseY + offsetB;
        }
        Gui.drawRect(this.left, this.top, this.right, this.bottom, this.color);
        if(this.hasText){
            float scale = 0.9F;
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale);
            FontUtil.normal.drawCenteredString(name, (this.left + (this.right - this.left) / 2) / scale,
                    (this.top + (this.bottom - this.top - FontUtil.normal.getHeight()) / 2 + 1) / scale, -1);
            GlStateManager.popMatrix();
        }
    }

}
