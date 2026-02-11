package peak.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import peak.Client;
import peak.managers.Rendermanager;
import peak.managers.font.FontUtil;
import peak.modules.player.Scaffold;
import peak.modules.render.HUDMod;
import peak.ui.arraylists.Arraylist;

public class HUD extends GuiScreen {

    public static Minecraft mc = Minecraft.getMinecraft();
    public static FontRenderer fr = mc.fontRendererObj;

    public static Scaffold scaffold = (Scaffold) Client.getModulebyName("Scaffold");
    public static HUDMod hudMod = (HUDMod) Client.getModulebyName("HUD");

    public static void init() {

        drawLogo();
        Arraylist.draw();

        if(scaffold.toggled) {
            Scaffold.renderBlockCount();
        }

        if(hudMod.toggled && hudMod.renderTest.isTrue()) {
            renderTest();
        }

    }

    private static void drawLogo() {
        FontUtil.normal.drawString(Client.name + " " + Client.version, 10, 10, -1);
    }

    private static void renderTest() {
        //Draw regular GuiRect
        Gui.drawRect(100, 100, 150, 125, 0xAA000000);

        //Draw GuiRect with round corners
        //Rendermanager.drawRoundedRect(300, 100, 350, 125, 0xAA000000);

        Rendermanager.drawCorner(250, 150, 30, 0);
    }

}
