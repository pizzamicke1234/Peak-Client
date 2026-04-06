package peak.ui.clickguis.classic.elements;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
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

    public void draw(int mouseX, int mouseY) {
        if (!moduleRect.showSettings) return;

        int settingSize = getSettingsSize();

        int left = this.moduleRect.categoryrect.right;
        int top = this.moduleRect.categoryrect.top + this.moduleRect.offsetY;
        int right = left + 30 + getRectWidth();
        int bottom = top + (lineHeight * settingSize);

        Gui.drawRect(left, top, right, bottom, 0x99111111);

        lines = 0;
        for (Setting s : this.settings) {

            s.updateStatus();

            if(!s.display){
                continue;
            }

            if (s instanceof ModeSetting) {
                drawModeLine(s, left + 5, top, left, top, right, bottom);
            } else if (s instanceof NumberSetting) {
                drawNumberLine(s, left + 5, top, left, top, right, bottom, mouseX, mouseY);
            } else if (s instanceof BoolSetting) {
                drawBoolLine(s, left + 5, top);
            }
            lines++;
        }
    }

    private void drawBoolLine(Setting s, int x, int y) {
        BoolSetting boolsetting = (BoolSetting) s;
        GlStateManager.color(255, 255, 255);
        if(boolsetting.isTrue()) {
            FontUtil.smaller.drawString(s.name, x, y + (lineHeight * lines) + 7, 0xff0069ff);
        }else {
            FontUtil.smaller.drawString(s.name, x, y + (lineHeight * lines) + 7, -1);
        }
    }

    private void drawModeLine(Setting s, int x, int y, int left, int top, int right, int bottom) {
        FontUtil.smaller.drawString(s.name + ":", x, y + (lineHeight * lines) + 7, -1);
        String val = s.currentValue;
        int valX = right - (int) FontUtil.smaller.getStringWidth(val) - 5;
        FontUtil.smaller.drawString(val, valX, y + (lineHeight * lines) + 7, 0xff0069ff);
    }

    private void drawNumberLine(Setting s, int x, int y, int left, int top, int right, int bottom, int mouseX, int mouseY) {
        NumberSetting num = (NumberSetting) s;

        int sliderWidth = 60;
        int sliderRight = right - 5;
        int sliderLeft = sliderRight - sliderWidth;

        int sliderTop = y + (lineHeight * lines) + 10;
        int sliderBottom = sliderTop + 2;

        if (num.dragging) {
            double diff = num.maxValue - num.minValue;
            double percent = (double)(mouseX - sliderLeft) / (double)sliderWidth;
            percent = Math.min(1, Math.max(0, percent));
            double newValue = num.minValue + (percent * diff);

            double precision = 1.0 / num.increment;
            num.cValue = Math.round(newValue * precision) / precision;
            num.currentValue = String.valueOf(num.cValue);
        }

        FontUtil.smaller.drawString(s.name + ": " + num.cValue, x, y + (lineHeight * lines) + 7, -1);

        Gui.drawRect(sliderLeft, sliderTop, sliderRight, sliderBottom, 0xff000000);

        double renderPercent = (num.cValue - num.minValue) / (num.maxValue - num.minValue);
        int barRight = sliderLeft + (int)(renderPercent * sliderWidth);
        Gui.drawRect(sliderLeft, sliderTop, barRight, sliderBottom, 0xff0069ff);
    }

    private int getSettingsSize() {
        int size = 0;
        for(Setting s : settings) {
            if(s.display) {
                size++;
            }
        }
        return size;
    }

    /*private void drawNumberLine(Setting s, int x, int y, int left, int top, int right, int bottom, int mouseX, int mouseY) {
        NumberSetting num = (NumberSetting) s;

        int sliderWidth = 60;
        int sliderRight = right - 5;
        int sliderLeft = sliderRight - sliderWidth;
        int sliderTop = y + (lineHeight * lines) + 9;
        int sliderBottom = y + (lineHeight * lines) + 11;

        if (num.dragging) {
            double diff = num.maxValue - num.minValue;
            double percent = (double)(mouseX - sliderLeft) / (double)sliderWidth;
            percent = Math.min(1, Math.max(0, percent));
            double newValue = num.minValue + (percent * diff);

            double precision = 1.0 / num.increment;
            num.cValue = Math.round(newValue * precision) / precision;
            num.current_value = String.valueOf(num.cValue);
        }

        FontUtil.smaller.drawString(s.name + ": " + num.cValue, x, y + (lineHeight * lines) + 6, -1);

        Gui.drawRect(sliderLeft, sliderTop, sliderRight, sliderBottom, 0xff000000);

        double renderPercent = (num.cValue - num.minValue) / (num.maxValue - num.minValue);
        int barRight = sliderLeft + (int)(renderPercent * sliderWidth);
        Gui.drawRect(sliderLeft, sliderTop, barRight, sliderBottom, 0xff0069ff);
    }*/

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        if (!moduleRect.showSettings) return;

        int left = this.moduleRect.categoryrect.right;
        int top = this.moduleRect.categoryrect.top + this.moduleRect.offsetY;
        int right = left + 30 + getRectWidth();
        int sliderLeft = right - 5 - 60;

        int currentLine = 0;
        for (Setting s : this.settings) {

            if(!s.display) {
                continue;
            }

            int lineTop = top + (lineHeight * currentLine);
            int lineBottom = lineTop + lineHeight;

            if (s instanceof ModeSetting) {
                if (mouseX >= left && mouseX <= right && mouseY >= lineTop && mouseY <= lineBottom) {
                    if (mouseButton == 0) onModeClick((ModeSetting) s);
                }
            } else if (s instanceof NumberSetting) {

                if (mouseX >= sliderLeft && mouseX <= right && mouseY >= lineTop && mouseY <= lineBottom) {
                    if (mouseButton == 0) ((NumberSetting) s).dragging = true;
                }
            }else if (s instanceof BoolSetting) {

                if (mouseX >= left && mouseX <= right && mouseY >= lineTop && mouseY <= lineBottom) {
                    if (mouseButton == 0) onBoolClick((BoolSetting) s);
                }
            }
            currentLine++;
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (Setting s : this.settings) {
            if (s instanceof NumberSetting) {
                ((NumberSetting) s).dragging = false;
            }
        }
    }

    private void onModeClick(ModeSetting s) {
        s.nextMode();
    }

    private void onBoolClick(BoolSetting s) {
        s.toggle();
    }

    public int getRectWidth() {
        int longestWidth = 0;
        for(Setting s : this.settings) {
            int sWidth = 0;
            sWidth = (int)Math.ceil(FontUtil.normal.getStringWidth(s.name));
            if(s instanceof ModeSetting) {
                sWidth += getLongestValueWidth((ModeSetting) s);
            } else if (s instanceof NumberSetting) {
                sWidth += getNumberSettingWidth((NumberSetting) s);
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

    public int getNumberSettingWidth(NumberSetting s) {
        //return (int)Math.ceil(s.maxValue - s.minValue);
        return 65;
    }
}