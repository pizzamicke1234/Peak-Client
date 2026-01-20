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
import java.util.ArrayList;

public class ClickGui extends GuiScreen {

    public Minecraft mc = Minecraft.getMinecraft();
    public FontRenderer fr = mc.fontRendererObj;

    ArrayList<MovableRect> movableRects = new ArrayList<MovableRect>();

    int c = 0;

    public void initGui()
    {
        if(c == 0) {
            movableRects.add(new MovableRect(50, 50, 70, 70, 0xFF000000));
            c++;
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //this.drawDefaultBackground();
        fr.drawStringWithShadow("Test", 100, 100, 1);
        drawRects();
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

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton == 0) {

            for(MovableRect mr : movableRects) {
                if(mr.isClicked(mouseX, mouseY)) {
                    System.out.println("A Rect was clicked!");
                }
            }

        }

    }

    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
        if(clickedMouseButton == 0) {

            for(MovableRect mr : movableRects) {
                if(mr.isClicked(mouseX, mouseY)) {
                    mr.move(mouseX, mouseY);
                }
            }

        }
    }

    public void drawRects() {
        for(MovableRect mr : movableRects) {
            mr.draw();
        }
    }

}
