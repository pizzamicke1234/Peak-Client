package peak.ui.mainmenus;

import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import peak.altmanager.GuiAltManager;
import peak.ui.mainmenus.elements.PeakButton;

import java.util.ArrayList;

public class PeakMainMenu extends GuiScreen {

    public final ResourceLocation background = new ResourceLocation("peak/backgrounds/menu1.png");
    public final ResourceLocation logo = new ResourceLocation("peak/hud/logonew.png");


    PeakButton btnsingleplayer, btnmultiplayer, btnaltmanager, btnoptions, btnexit, btnToggleBackground;
    ArrayList<PeakButton> buttons;

    @Override
    public void initGui() {

        buttons = new ArrayList<>();

        btnsingleplayer = new PeakButton(0, this.width / 2 - 60, this.height / 2 - 10, 120, 22, "Singeplayer");
        btnmultiplayer = new PeakButton(1, this.width / 2 - 60, this.height / 2 - 10 + 30, 120, 22, "Multiplayer");
        btnaltmanager = new PeakButton(2, this.width / 2 - 60, this.height / 2 - 10 + 60, 120, 22, "AltManager");
        btnoptions = new PeakButton(3, this.width / 2 - 60, this.height / 2 - 10 + 90, 120, 22, "Options");
        btnexit = new PeakButton(4, this.width / 2 - 60, this.height / 2 - 10 + 120, 120, 22, "Exit");

        btnToggleBackground = new PeakButton(5, width - 75, height - 25, 70, 20, "Background");

        buttons.add(btnsingleplayer);
        buttons.add(btnmultiplayer);
        buttons.add(btnaltmanager);
        buttons.add(btnoptions);
        buttons.add(btnexit);
        //buttons.add(btnToggleBackground);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackgroundandLogo();
        drawButtons(mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseButton == 0) {
            for(PeakButton button : buttons) {

                if(button.isClicked(mouseX, mouseY)) {
                    switch (button.buttonID) {
                        case 0:
                            this.mc.displayGuiScreen(new GuiSelectWorld(this));
                            break;

                        case 1:
                            this.mc.displayGuiScreen(new GuiMultiplayer(this));
                            break;

                        case 2:
                            this.mc.displayGuiScreen(new GuiAltManager());
                            break;

                        case 3:
                            this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                            break;

                        case 4:
                            this.mc.shutdown();
                            break;
                    }
                }

            }
        }
    }

    public void drawButtons(int mouseX, int mouseY) {
        for(PeakButton button : buttons) {
            button.drawButton(mouseX, mouseY);
        }
    }

    public void drawBackgroundandLogo() {
        this.mc.getTextureManager().bindTexture(background);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, (float)this.width, (float)this.height);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        this.mc.getTextureManager().bindTexture(logo);

        int logoWidth = 300;
        int logoHeight = 200;
        Gui.drawModalRectWithCustomSizedTexture(this.width / 2 - logoWidth / 2, this.height / 2 - logoHeight - 30, 0, 0, logoWidth, logoHeight, (float)logoWidth, (float)logoHeight);

        net.minecraft.client.renderer.GlStateManager.disableBlend();
    }

}
