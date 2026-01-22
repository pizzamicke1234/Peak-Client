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

    public boolean showSettings = false;

    final boolean hasText;
    public int offsetY, color, hovercolor;
    int thickness = 20;

    public SettingsRect settingsRect;

    public ModuleRect(Module module, CategoryRect categoryrect,int offsetY, int color, int hovercolor, boolean hasText) {
        this.module = module;
        this.offsetY = offsetY;
        this.color = color;
        this.hovercolor = hovercolor;
        this.hasText = hasText;
        this.categoryrect = categoryrect;
        this.settingsRect = new SettingsRect(this);
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (showSettings) {
            settingsRect.mouseReleased(mouseX, mouseY, state);
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0 && isHovered(mouseX, mouseY)) {
            module.toggle();
        }else if(mouseButton == 1 && isHovered(mouseX, mouseY)) {
            if(module.getSettings().size() > 0) showSettings = !showSettings;
        }

        if (showSettings) {
            settingsRect.mouseClicked(mouseX, mouseY, mouseButton);
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

        int x1 = left + (right - left) / 2;
        int y1 = top + (bottom - top - FontUtil.normal.getHeight()) / 2 + 1;

        // Draw SettingRect
        this.settingsRect.draw(x, y);

        if(this.hasText){

            int c;

            if(module.toggled) c = 0xff0069ff;
            else c = -1;

            FontUtil.normal.drawCenteredString(module.name, x1 , y1, c);
        }
    }

}