package peak.ui.clickguis.classic;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import peak.Client;
import peak.modules.Module;
import peak.modules.render.ClickGuimod;
import peak.ui.clickguis.classic.elements.CategoryRect;
import peak.ui.clickguis.classic.elements.ModuleRect;

import java.util.ArrayList;

public class ClickGui extends GuiScreen {

    public Minecraft mc = Minecraft.getMinecraft();
    public FontRenderer fr = mc.fontRendererObj;

    private final ClickGuimod clickGuimod = (ClickGuimod) Client.getModulebyName("ClickGui");

    public ArrayList<CategoryRect> categoryRects = clickGuimod.categoryRects;
    public ArrayList<ModuleRect> moduleRects = clickGuimod.moduleRects;

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
        drawClickGui(mouseX, mouseY);

    }

    @Override
    public void onGuiClosed()
    {
        Client.getModulebyName("ClickGui").disable();

        //Save Elements
        clickGuimod.categoryRects = categoryRects;
        clickGuimod.moduleRects = moduleRects;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if(keyCode == Keyboard.KEY_RSHIFT || keyCode == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for(CategoryRect cr : categoryRects) {
            cr.mouseClicked(mouseX, mouseY, mouseButton);
        }
        for(ModuleRect mr : moduleRects) {
            mr.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for(CategoryRect mr : categoryRects) {
            mr.mouseReleased(mouseX, mouseY, state);
        }
        for(ModuleRect mr : moduleRects) {
            mr.mouseReleased(mouseX, mouseY, state);
        }
    }

    public void initClickGui() {
        int categorycount = 0;
        int categoryoffset = 150;

        if(categoryRects.size() == 0) {
            for(Module.Category c : Module.Category.values()) {
                int modulecount = 0;
                CategoryRect categoryRect = new CategoryRect(c.name(), 50 + (categoryoffset * categorycount),
                        50, 130 + (categoryoffset * categorycount), 70, 0xBB000000, true);
                categoryRects.add(categoryRect);

                for(Module m : Client.modules) {
                    if(m.category == c && m.inClickGui) {
                        ModuleRect moduleRect = new ModuleRect(m, categoryRect, 20 + (20 * modulecount), 0x77000000, 0x44000000, true);
                        moduleRects.add(moduleRect);
                        modulecount++;
                    }
                }
                categorycount++;
            }
        }
    }

    public void drawCategories(int x, int y) {
        for(CategoryRect cr : categoryRects) {
            cr.draw(x, y);
        }
    }

    public void drawModules(int x, int y) {
        for(ModuleRect mr : moduleRects) {
            mr.draw(x, y);
        }
    }

    public void drawClickGui(int x, int y) {
        drawCategories(x, y);
        drawModules(x, y);
    }

}
