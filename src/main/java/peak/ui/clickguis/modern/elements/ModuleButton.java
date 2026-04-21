package peak.ui.clickguis.modern.elements;

import peak.managers.font.FontUtil;
import peak.managers.render.RenderManager;
import peak.modules.Module;

import java.awt.*;

public class ModuleButton {

    public Module module;

    public ModuleButton(Module module) {
        this.module = module;
    }

    public void draw(int x, int y, int width, int height) {
        RenderManager.drawRoundedRect(x, y, width, height, 6, new Color(50, 50, 50, 230));
        FontUtil.normal.drawString(module.name, x + 15, y + (float) height / 2 - ((float) FontUtil.normal.getHeight() / 2), -1);
    }

    public void draw(int x, int y, int width, int height, int visibleHeight) {
        RenderManager.drawRoundedRect(x, y, width, height, visibleHeight,  6, new Color(50, 50, 50, 230));

        int visibleHeightText = visibleHeight - height / 2 + FontUtil.normal.getHeight() / 2;
        if(visibleHeightText < 0) visibleHeightText = 0;

        RenderManager.drawClippedString(FontUtil.normal, module.name, x + 15,
                y + (float) height / 2 - ((float) FontUtil.normal.getHeight() / 2),
                visibleHeightText, -1);
    }

    public void drawWithOffset(int x, int y, int width, int height, int Yoffset) {

        Yoffset = Math.min(Yoffset, y + height);

        RenderManager.drawRoundedRectC(x, y, width, height, Yoffset,  6, new Color(50, 50, 50, 230));

        FontUtil.normal.drawString(module.name, x + 15,
                y + (float) height / 2 - ((float) FontUtil.normal.getHeight() / 2), -1);
    }

}
