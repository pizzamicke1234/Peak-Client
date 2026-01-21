package peak.ui.arraylists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import peak.Client;
import peak.managers.font.FontUtil;
import peak.modules.Module;
import peak.modules.settings.Setting;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Arraylist extends GuiScreen {

    public static Minecraft mc = Minecraft.getMinecraft();
    public static FontRenderer fr = mc.fontRendererObj;

    static List<Module>  modules = sortModules();

    public static void draw() {

        drawModules();
        /*int count = 0;
        for(Module m : modules) {
            if(!m.toggled) {
                continue;
            }

            Gui.drawRect(width  - (int)FontUtil.normal.getStringWidth(m.name) - 7,
                     (2 + FontUtil.normal.getHeight()) * count, width,
                    FontUtil.normal.getHeight() + 2 + (2 + FontUtil.normal.getHeight()) * count, 0x78000000);

            FontUtil.normal.drawString(m.name, width - FontUtil.normal.getStringWidth(m.name) - 4,
                    1 + (2 + FontUtil.normal.getHeight()) * count, -1);

            count++;

        }*/
    }

    public static int getModuleTextWidth(Module m) {
        int settingsWidth = 0;

        if(m.getSettings() != null) {
            for(Setting s : m.getSettings()) {
                if(s.showonArraylist) settingsWidth += FontUtil.normal.getStringWidth(s.current_value);
            }
        }

        return (int)(FontUtil.normal.getStringWidth(m.name) + settingsWidth);
    }

    public static List<Module> sortModules() {
        List<Module> sortedModules = Client.modules.stream()
                .sorted(Comparator.<Module>comparingInt(m -> getModuleTextWidth(m)).reversed())
                .collect(Collectors.toList());
        return sortedModules;
    }

    public static void drawModuleLine(Module m, int count) {
        int moduleOffsetX = getSettingsOffset(m);
        int settingsoffsetX = 0;

        Gui.drawRect(width  - (int)FontUtil.normal.getStringWidth(m.name) - (7 - moduleOffsetX),
                (2 + FontUtil.normal.getHeight()) * count, width,
                FontUtil.normal.getHeight() + 2 + (2 + FontUtil.normal.getHeight()) * count, 0x55000000);

        FontUtil.normal.drawString(m.name, width - FontUtil.normal.getStringWidth(m.name) - 4 + moduleOffsetX,
                1 + (2 + FontUtil.normal.getHeight()) * count, -1);

        if(m.getSettings() != null) {
            for(Setting s : m.getSettings()) {
                if(s.showonArraylist) {
                    FontUtil.normal.drawString(s.current_value, width - FontUtil.normal.getStringWidth(s.current_value) - 4 + settingsoffsetX,
                            1 + (2 + FontUtil.normal.getHeight()) * count, 0xFFcecece);

                    settingsoffsetX -= (5 + FontUtil.normal.getStringWidth(s.current_value));
                }
            }
        }
    }

    public static int getSettingsOffset(Module m) {
        int settingsOffset = 0;

        if(m.getSettings() != null) {
            for(Setting s : m.getSettings()) {
                if(s.showonArraylist) settingsOffset -= (5 + FontUtil.normal.getStringWidth(s.current_value));
            }
        }
        return settingsOffset;
    }

    public static void drawModules() {
        int count = 0;
        for(Module m : modules) {
            if(!m.toggled) continue;
            drawModuleLine(m, count);
            count++;
        }
    }

}
