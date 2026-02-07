package peak.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import peak.Client;
import peak.managers.font.FontUtil;
import peak.modules.player.Scaffold;
import peak.ui.arraylists.Arraylist;

public class HUD extends GuiScreen {

    public static Minecraft mc = Minecraft.getMinecraft();
    public static FontRenderer fr = mc.fontRendererObj;

    public static Scaffold scaffold = (Scaffold) Client.getModulebyName("Scaffold");

    public static void init() {

        draw_logo();
        Arraylist.draw();

        if(scaffold.toggled) {
            Scaffold.renderBlockCount();
        }

    }

    static void draw_logo() {

        FontUtil.normal.drawString(Client.name + " " + Client.version, 10, 10, -1);

    }

}
