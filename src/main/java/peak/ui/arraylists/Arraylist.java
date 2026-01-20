package peak.ui.arraylists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import peak.Client;
import peak.managers.font.FontUtil;
import peak.modules.Module;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Arraylist extends GuiScreen {

    public static Minecraft mc = Minecraft.getMinecraft();
    public static FontRenderer fr = mc.fontRendererObj;

    static List<Module>  modules = sortModules();

    public static void draw() {

        int count = 0;
        for(Module m : modules) {
            if(!m.toggled) {
                continue;
            }

            FontUtil.normal.drawString(m.name, width - FontUtil.normal.getStringWidth(m.name) - 5, 3 + (2 + fr.FONT_HEIGHT) * count, -1);
            count++;

        }
    }

    public static List<Module> sortModules() {
        List<Module> sortedModules = Client.modules.stream()
                .sorted(Comparator.<Module>comparingInt(m -> (int)FontUtil.normal.getStringWidth(m.getName())).reversed())
                .collect(Collectors.toList());
        return sortedModules;
    }

}
