package peak.ui.clickguis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import peak.Client;
import peak.modules.Module;
import peak.modules.render.ClickGuimod;

import java.awt.*;

public class ClickGui extends GuiScreen {

    public Minecraft mc = Minecraft.getMinecraft();
    public FontRenderer fr = mc.fontRendererObj;

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //this.drawDefaultBackground();
        fr.drawStringWithShadow("Test", 100, 100, 1);
        Gui.drawRect(100, 100, 200, 200,0xFF121111);
    }

    public void onGuiClosed()
    {
        //idfk how to do it otherwise
        for(Module m : Client.modules) {
            if(m.name.equalsIgnoreCase("ClickGui")) {
                m.disable();
            }
        }
    }

    protected void keyTyped(char typedChar, int keyCode) {
        if(keyCode == Keyboard.KEY_RSHIFT || keyCode == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(null);
        }
    }

    public void testdraw() {
        Gui.drawRect(60, 60, 80, 80, 1);
    }

}
