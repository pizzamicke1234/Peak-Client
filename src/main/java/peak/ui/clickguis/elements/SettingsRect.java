package peak.ui.clickguis.elements;

import net.minecraft.client.gui.Gui;
import peak.managers.font.FontUtil;
import peak.modules.settings.BoolSetting;
import peak.modules.settings.ModeSetting;
import peak.modules.settings.NumberSetting;
import peak.modules.settings.Setting;

import java.util.ArrayList;

public class SettingsRect {

    public ModuleRect moduleRect;
    public ArrayList<Setting> settings;
    public int lines;
    public int lineHeight;

    public SettingsRect(ModuleRect moduleRect) {
        this.moduleRect = moduleRect;
        this.settings = moduleRect.module.getSettings();
        this.lineHeight = this.moduleRect.thickness;
    }

    public void draw(int x, int y) {

        if(moduleRect.showSettings) {
            drawLines();
        }

    }

    public void drawLines() {
        int left = this.moduleRect.categoryrect.right;
        int top = this.moduleRect.categoryrect.top + this.moduleRect.offsetY;
        int right = left + 30 + getRectWidth();
        int bottom = top + this.moduleRect.thickness;

        int thickness = 1;

        Gui.drawRect(left, top, right, bottom + (lineHeight * (lines - 1)), 0xEE000000);
        Gui.drawRect(left + thickness, top + thickness, right - thickness, bottom - thickness + (lineHeight * (lines - 1)), 0xEE666666);
        lines = 0;
        for(Setting s : this.settings) {
            if(s instanceof ModeSetting) drawModeLine(s, left + 5, top + 3, left, top, right, bottom, lines);
            if(s instanceof NumberSetting) drawNumberLine(s, left + 5, top + 3, left, top, right, bottom, lines);
            lines++;
        }

    }

    public void drawModeLine(Setting s, int x, int y, int left, int top, int right, int bottom, int line) {
        FontUtil.normal.drawString(s.name + ":", x, y + (lineHeight * lines), -1);

        Gui.drawRect(x + getRectWidth() - (int)FontUtil.normal.getStringWidth(s.current_value) + 16, top + 2 + (lineHeight * lines),
                right - 2, bottom - 2 + (lineHeight * lines), 0xEE000000);

        FontUtil.normal.drawString(s.current_value, x + getRectWidth() - FontUtil.normal.getStringWidth(s.current_value) + 20, y + (lineHeight * lines), -1);
    }

    public void drawNumberLine(Setting s, int x, int y, int left, int top, int right, int bottom, int line) {
        FontUtil.normal.drawString(s.name + ":", x, y + (lineHeight * lines), -1);

        Gui.drawRect(x + getRectWidth() - (int)FontUtil.normal.getStringWidth(s.current_value) + 11, top + 4 + (lineHeight * lines),
                right - 2, bottom - 4 + (lineHeight * lines), 0xEE000000);
    }
    public void drawBoolLine() {

    }

    public int getRectWidth() {
        int longestWidth = 0;
        for(Setting s : this.settings) {
            int sWidth = (int)Math.ceil(FontUtil.normal.getStringWidth(s.name));
            if(s instanceof ModeSetting) {
                sWidth += getLongestValueWidth((ModeSetting) s);
            }
            if(sWidth > longestWidth) {
                longestWidth = sWidth;
            }
        }
        return longestWidth;
    }

    public int getLongestValueWidth(ModeSetting s) {
        int longestWidth = 0;
        for(String value : s.modes) {
            int modeWidth = (int)Math.ceil(FontUtil.normal.getStringWidth(value));
            if(modeWidth > longestWidth) longestWidth = modeWidth;
        }
        return longestWidth;
    }

}
