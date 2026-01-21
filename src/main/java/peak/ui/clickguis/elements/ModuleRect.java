package peak.ui.clickguis.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import peak.managers.font.FontUtil;
import peak.modules.Module;


public class ModuleRect {

    public Minecraft mc = Minecraft.getMinecraft();

    public Module module;
    public CategoryRect categoryrect;

    final boolean hasText;
    public int offsetY, color, hovercolor;
    int thickness = 20;

    public ModuleRect(Module module, CategoryRect categoryrect,int offsetY, int color, int hovercolor, boolean hasText) {
        this.module = module;
        this.offsetY = offsetY;
        this.color = color;
        this.hovercolor = hovercolor;
        this.hasText = hasText;
        this.categoryrect = categoryrect;
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isHovered(mouseX, mouseY)) {
            System.out.println("Toggled module: " + module.name);
            module.toggle();
        }
    }

    private boolean isHovered(int mouseX, int mouseY) {
        if(mouseX >= this.categoryrect.left && mouseX <= categoryrect.right &&
                mouseY >= (categoryrect.top + this.offsetY) && mouseY <= (categoryrect.top + this.offsetY + thickness)) {
            return true;
        }
        return false;
    }

    public void draw(int x, int y) {
        int left = categoryrect.left;
        int top = categoryrect.top + this.offsetY;
        int right = categoryrect.right;
        int bottom = top + thickness;

        int rectcolor;

        if(isHovered(x, y)) rectcolor = this.hovercolor;
        else rectcolor = this.color;

        Gui.drawRect(left, top, right, bottom, rectcolor);

        if(this.hasText){
            float scale = 0.9F;
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale);

            int c;

            if(module.toggled) c = 0xff0069ff;
            else c = -1;

            FontUtil.normal.drawCenteredString(module.name, (left + (right - left) / 2) / scale,
                    (top + (bottom - top - FontUtil.normal.getHeight()) / 2 + 1) / scale, c);

            GlStateManager.popMatrix();
        }
    }

}