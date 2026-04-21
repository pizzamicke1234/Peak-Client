package peak.ui.clickguis.modern;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import peak.Client;
import peak.managers.font.FontUtil;
import peak.managers.render.RenderManager;
import peak.modules.Module;
import peak.modules.Module.Category;
import peak.modules.render.ClickGuimod;
import peak.ui.clickguis.modern.elements.CategoryTab;
import peak.ui.clickguis.modern.elements.ModuleButton;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class ClickGuiModern extends GuiScreen {

    private Minecraft mc = Minecraft.getMinecraft();
    private final ClickGuimod clickGuiModule = (ClickGuimod) Client.getModulebyName("ClickGui");

    private ArrayList<CategoryTab> categoryTabs;
    private Category selectedCategory = Category.COMBAT;

    private ArrayList<ModuleButton> moduleButtons = new ArrayList<>();

    private int moduleScrollOffset = 0;
    private int moduleScrollOffsetMax;

    @Override
    public void initGui() {

        int offset = 0;
        categoryTabs = new ArrayList<>();
        for(Category category : Category.values()) {
            final int x = width / 2 - (450 / 2) + 10 + 6;
            final int y = height / 2 - (280 / 2) + 16;
            final int width = (450 / 4) - 12;
            final int height = 25;

            CategoryTab categoryTab = new CategoryTab(category, x, y + offset, width, height);
            categoryTabs.add(categoryTab);
            offset += height + 8;
        }
        categoryChanged();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground();

        for(CategoryTab categoryTab : categoryTabs) {
            categoryTab.draw(mouseX, mouseY);
        }

        if(moduleButtons.size() > 6) {
            if(moduleScrollOffset < moduleScrollOffsetMax) {
                moduleScrollOffset = moduleScrollOffsetMax;
            } else if (moduleScrollOffset > 0) {
                moduleScrollOffset = 0;
            }
        }

        int c = 0;
        for(ModuleButton moduleButton : moduleButtons) {
            final int x = (int) ((float) width / 2 - (450f / 2) + 10 + (450f / 4f) + 20);
            final int y = (height / 2 - (280 / 2) + 15) + c + moduleScrollOffset;
            final int width = (int) (450f * 0.75f - 50);
            final int height = 35;

            final int minHeight = (this.height / 2 - (280 / 2) + 15);
            final int maxHeight = (this.height / 2 - (280 / 2) + 10) + 260;

            if(y > maxHeight) break;
            if(y + height < minHeight) {
                c += height + 5;
                continue;
            }

            if(minHeight <= y && y + height < maxHeight) {
                moduleButton.draw(x, y, width, height);

            } else if (y < minHeight) {
                int Yoffset = minHeight - y;
                moduleButton.drawWithOffset(x, y, width, height, Yoffset);
                System.out.println("OFFset: "+ Yoffset);

            } else {
                int visibleHeight = height - ((y + height) - maxHeight) - 5;
                moduleButton.draw(x, y, width, height, visibleHeight);
                break;
            }

            c += height + 5;
        }

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
                    categoryChanged();
                }
                break;
            }
        }


    }

    @Override
    public void onMouseScroll(int mouseX, int mouseY, boolean up) {
        if(mouseInModuleArea(mouseX, mouseY) && moduleButtons.size() > 6) {
            moduleScrollOffset += up ? 10 : -10;
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

    private void categoryChanged() {
        for(CategoryTab categoryTab : categoryTabs) {
            if(categoryTab.category == selectedCategory) {
                categoryTab.selected = true;
                continue;
            }
            categoryTab.selected = false;
        }

        moduleButtons.clear();
        for(Module m : Client.modules) {
            if(m.category != selectedCategory) continue;

            ModuleButton moduleButton = new ModuleButton(m);
            moduleButtons.add(moduleButton);
        }
        moduleScrollOffset = 0;
        moduleScrollOffsetMax = getMaxScrollOffset();
    }

    private boolean mouseInModuleArea(int mouseX, int mouseY) {
        final int rectWidth = 450;
        final int rectHeight = 280;
        final int x = width / 2 - (rectWidth / 2) + (rectWidth / 4) + 20;
        final int y = height / 2 - (rectHeight / 2) + 10;
        final int width = (int) (rectWidth * 0.75f - 30);
        final int height = rectHeight - 20;

        return mouseX > x && mouseX < (x + width) && mouseY > y && mouseY < (y + height);
    }

    private int getMaxScrollOffset() {
        return moduleButtons.size() * -40 + 40 + 215;
    }

}
