package peak.ui.clickguis.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import peak.modules.Module;


public class ModuleRect {

    public Minecraft mc = Minecraft.getMinecraft();

    public Module module;
    public CategoryRect categoryrect;

    final boolean hasText;
    public int offsetY, color;
    int thickness = 20;

    public ModuleRect(Module module, CategoryRect categoryrect,int offsetY, int color, boolean hasText) {
        this.module = module;
        this.offsetY = offsetY;
        this.color = color;
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

    public void draw() {
        int left = categoryrect.left;
        int top = categoryrect.top + this.offsetY;
        int right = categoryrect.right;
        int bottom = top + thickness;

        Gui.drawRect(left, top, right, bottom, this.color);
        if(this.hasText){
            Gui.drawCenteredStringWithoutShadow(mc.fontRendererObj, module.name, left + (right - left) / 2,
                    top + (bottom - top) / 2 - 3, -1);
        }
    }

}