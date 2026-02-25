package peak.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import peak.Client;
import peak.managers.ColorManager;
import peak.managers.font.FontUtil;
import peak.managers.render.RenderManager;
import peak.modules.player.Scaffold;
import peak.modules.render.HUDMod;
import peak.ui.arraylists.Arraylist;
import peak.ui.notifications.NotificationManager;
import peak.ui.tagethuds.TargetHud;

public class HUD extends GuiScreen {

    public static Minecraft mc = Minecraft.getMinecraft();
    public static FontRenderer fr = mc.fontRendererObj;

    public static final ResourceLocation logoNew = new ResourceLocation("peak/backgrounds/Logonew.png");

    public static Scaffold scaffold = (Scaffold) Client.getModulebyName("Scaffold");
    public static HUDMod hudMod = (HUDMod) Client.getModulebyName("HUD");

    public static void init() {

        drawLogo();
        Arraylist.draw();
        NotificationManager.render();

        if(scaffold.toggled) {
            Scaffold.renderBlockCount();
        }

        if(hudMod.toggled && hudMod.renderTest.isTrue()) {
            renderTest();
        }

    }

    private static void drawLogo() {
        if(hudMod.logoMode.currentValue.equals("Classic")) {
            double scale = 1;
            GlStateManager.pushMatrix();
            GlStateManager.scale(scale, scale, scale);
            FontUtil.bigger.drawString(Client.name + " " + Client.version, 10, 10, -1);
            FontUtil.bigger.drawString(String.valueOf(Client.name.charAt(0)), 10, 10, ColorManager.getRainbowWave(3, 1));
            GlStateManager.popMatrix();
        }else {
            GlStateManager.pushMatrix();
            GlStateManager.color(255, 255, 255);
            mc.getTextureManager().bindTexture(logoNew);

            int logoWidth = 300 / 2;
            int logoHeight = 200 / 2;
            Gui.drawModalRectWithCustomSizedTexture(-20, -20, 0, 0, logoWidth, logoHeight, (float)logoWidth, (float)logoHeight);
            GlStateManager.popMatrix();
        }
    }

    private static void renderTest() {
        TargetHud.draw();
    }

}
