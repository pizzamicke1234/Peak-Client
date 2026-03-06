package peak.altmanager;

import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import peak.managers.font.FontUtil;
import peak.ui.mainmenus.PeakMainMenu;
import peak.ui.mainmenus.elements.PeakButton;

import java.io.IOException;
import java.util.ArrayList;

public class GuiAltManager extends GuiScreen {
    public final ResourceLocation background = new ResourceLocation("peak/backgrounds/menu1.png");
    private GuiTextField nameField;
    private PeakButton btnExit, btnMicrosoft, btnCracked;
    private ArrayList<PeakButton> buttons;

    @Override
    public void initGui() {
        buttons = new ArrayList<>();

        this.nameField = new GuiTextField(0, this.fontRendererObj, 10, 95, width / 5 - 20, 20);

        btnCracked = new PeakButton(0, 10, 120, width / 5 - 20, 20, "Cracked Login");
        btnMicrosoft = new PeakButton(1, 10, 150, width / 5 - 20, 20, "Microsoft Login");
        btnExit = new PeakButton(2, this.width / 2 - 80, this.height - 40, 160,
                20, "Close");

        buttons.add(btnCracked);
        buttons.add(btnMicrosoft);
        buttons.add(btnExit);

        this.nameField.setFocused(true);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        this.mc.getTextureManager().bindTexture(background);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, (float)this.width, (float)this.height);

        drawBackground();

        this.nameField.drawTextBox();

        for(PeakButton button : buttons) {
            button.drawButton(mouseX, mouseY);
        }

        FontUtil.normal.drawCenteredString("Current Account", this.width / 2, 15, -1);
        FontUtil.normal.drawCenteredString(mc.session.getUsername(), this.width / 2, 30, 0xFF00FF00);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        this.nameField.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == 1) {
            this.mc.displayGuiScreen(new PeakMainMenu());
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        for(PeakButton button : buttons) {
            if(button.isClicked(mouseX, mouseY)) {
                switch (button.buttonID) {
                    case 0:
                        if (!nameField.getText().isEmpty()) {
                            mc.session = new Session(nameField.getText(), "0", "0", "legacy");
                        }
                        break;

                    case 1:
                        LoginWithMicrosoftWeb();
                        break;

                    case 2:
                        this.mc.displayGuiScreen(new PeakMainMenu());
                        break;
                }
            }
        }

        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void LoginWithMicrosoftWeb() {
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();

        authenticator.loginWithAsyncWebview().thenAccept(result -> {

            mc.session = new Session(
                    result.getProfile().getName(),
                    result.getProfile().getId(),
                    result.getAccessToken(),
                    "microsoft"
            );

            System.out.println("Login succesful: " + result.getProfile().getName());

        }).exceptionally(ex -> {
            System.out.println("Login Error: " + ex.getMessage());
            return null;
        });
    }

    private void drawBackground() {
        Gui.drawRect(0, 0, width, height, 0x11404040);

        Gui.drawRect(0, 0, width / 5, height, 0x255f009b);
        Gui.drawRect(width - width / 5, 0, width, height, 0x255f009b);
    }

}