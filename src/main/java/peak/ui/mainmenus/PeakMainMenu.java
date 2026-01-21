package peak.ui.mainmenus;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class PeakMainMenu extends GuiScreen {

    public final ResourceLocation background = new ResourceLocation("backgrounds/mainmenu.jpg");

    @Override
    public void initGui() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground();
    }

    public void drawBackground() {
        this.mc.getTextureManager().bindTexture(background);
        this.drawTexturedModalRect(0, 0, 0, 0, width, this.height);
    }

}
