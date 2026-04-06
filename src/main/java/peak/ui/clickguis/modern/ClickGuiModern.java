package peak.ui.clickguis.modern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import peak.Client;
import peak.managers.font.FontUtil;
import peak.managers.render.RenderManager;
import peak.modules.Module.Category;
import peak.modules.render.ClickGuimod;
import peak.ui.clickguis.modern.elements.CategoryTab;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ClickGuiModern extends GuiScreen {

    private Minecraft mc = Minecraft.getMinecraft();
    private final ClickGuimod clickGuiModule = (ClickGuimod) Client.getModulebyName("ClickGui");

    private ArrayList<CategoryTab> categoryTabs;
    private Category selectedCategory = Category.COMBAT;

    @Override
    public void initGui() {
        categoryTabs = new ArrayList<>();

        int offset = 0;
        for(Category category : Category.values()) {
            final int x = width / 2 - (450 / 2) + 10 + 6;
            final int y = height / 2 - (280 / 2) + 16;
            final int width = (450 / 4) - 12;
            final int height = 25;

            CategoryTab categoryTab = new CategoryTab(category, x, y + offset, width, height);
            categoryTabs.add(categoryTab);
            offset += height + 8;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground();

        for(CategoryTab categoryTab : categoryTabs) {
            categoryTab.draw(mouseX, mouseY);
        }

        FontUtil.normal.drawCenteredString(selectedCategory.name(), 100, 100, -1);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == Keyboard.KEY_RSHIFT || keyCode == Keyboard.KEY_ESCAPE) {
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        for(CategoryTab categoryTab : categoryTabs) {
            if(mouseButton == 0 && categoryTab.isHovered(mouseX, mouseY)) {
                if(selectedCategory != categoryTab.category) {
                    selectedCategory = categoryTab.category;
                }
                break;
            }
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {

    }

    @Override
    public void onGuiClosed() {
        Client.getModulebyName("ClickGui").disable();
    }

    private void drawBackground() {
        //Background
        final float rectWidth = 450;
        final float rectHeight = 280;
        RenderManager.drawRoundedRect((float) width / 2 - (rectWidth / 2), (float) height / 2 - (rectHeight / 2),
                rectWidth, rectHeight, 5, new Color(40, 40, 40, 230));

        //Tabs
        RenderManager.drawRoundedRect((float) width / 2 - (rectWidth / 2) + 10, (float) height / 2 - (rectHeight / 2) + 10,
                rectWidth / 4f, rectHeight - 20, 3, new Color(60, 60, 60, 200));

        //Module Area
        RenderManager.drawRoundedRect((float) width / 2 - (rectWidth / 2) + 10 + (rectWidth / 4f) + 10,
                (float) height / 2 - (rectHeight / 2) + 10, rectWidth * 0.75f - 30, rectHeight - 20,
                3, new Color(40, 40, 40, 200));
    }

}
