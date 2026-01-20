package peak.ui.clickguis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import peak.Client;
import peak.modules.Module;
import peak.ui.clickguis.elements.CategoryRect;

import java.util.ArrayList;

public class ClickGui extends GuiScreen {

    public Minecraft mc = Minecraft.getMinecraft();
    public FontRenderer fr = mc.fontRendererObj;

    ArrayList<CategoryRect> categoryRects = new ArrayList<CategoryRect>();

    boolean elementdraw = false;

    @Override
    public void initGui()
    {
        if(!elementdraw) {
            initClickGui();
            elementdraw = true;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //this.drawDefaultBackground();
        fr.drawStringWithShadow("Test", 100, 100, 1);

        drawClickGui(mouseX, mouseY);

    }

    @Override
    public void onGuiClosed()
    {
        //idfk how to do it otherwise
        for(Module m : Client.modules) {
            if(m.name.equalsIgnoreCase("ClickGui")) {
                m.disable();
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if(keyCode == Keyboard.KEY_RSHIFT || keyCode == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for(CategoryRect mr : categoryRects) {
            mr.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for(CategoryRect mr : categoryRects) {
            mr.mouseReleased(mouseX, mouseY, state);
        }
    }

    public void addCategories() {
        int count = 0;
        int offset = 100;
        for(Module.Category c : Module.Category.values()) {
            categoryRects.add(new CategoryRect(c.name(), 50 + (offset * count), 50, 130 + (offset * count), 70, 0xFF000000, true));
            count++;
        }
    }

    public void initClickGui() {
        addCategories();
    }

    public void drawCategories(int x, int y) {
        for(CategoryRect cr : categoryRects) {
            cr.draw(x, y);
        }
    }

    public void drawClickGui(int x, int y) {
        drawCategories(x, y);
    }

}
