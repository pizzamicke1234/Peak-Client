package peak.altmanager;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import peak.ui.mainmenus.PeakMainMenu;
import peak.ui.mainmenus.elements.PeakButton;

import java.io.IOException;

public class GuiAltManager extends GuiScreen {
    public final ResourceLocation background = new ResourceLocation("backgrounds/background1.png");
    private GuiTextField nameField, pwField;
    public PeakButton btnExit, btnMicrosoft, btnCracked;

    @Override
    public void initGui() {
        this.nameField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
        this.pwField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 90, 200, 20);

        btnCracked = new PeakButton(1, this.width / 2 - 100, 120, this.width / 2 + 100, 140, "Cracked Login");
        btnMicrosoft = new PeakButton(3, this.width / 2 - 100, 150, this.width / 2 + 100, 170, "Microsoft Login");
        btnExit = new PeakButton(2, this.width / 2 - 80, this.height - 40, this.width / 2 + 80,
                this.height - 20, "Close");

        this.nameField.setFocused(true);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        //this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(background);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, (float)this.width, (float)this.height);
        this.nameField.drawTextBox();
        this.pwField.drawTextBox();
        btnExit.drawButton(mouseX, mouseY);
        btnMicrosoft.drawButton(mouseX, mouseY);
        btnCracked.drawButton(mouseX, mouseY);
        this.drawCenteredString(this.fontRendererObj, "Current Account: " + mc.session.getUsername(), this.width / 2, 30, -1);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        this.nameField.textboxKeyTyped(typedChar, keyCode);
        this.pwField.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == 1) {
            this.mc.displayGuiScreen(new PeakMainMenu());
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {

        if(btnExit.isClicked(mouseX, mouseY)) this.mc.displayGuiScreen(new PeakMainMenu());

        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
        this.pwField.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(btnMicrosoft.isClicked(mouseX, mouseY)) {
            LoginWithMicrosoftWeb();
        }

        if(btnCracked.isClicked(mouseX, mouseY)) {
            if (!nameField.getText().isEmpty()) {
                mc.session = new Session(nameField.getText(), "0", "0", "legacy");
            }
        }

    }

    public void LoginWithMicrosoftWeb() {
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        // Der "Web"-Login öffnet ein Fenster für den User
        authenticator.loginWithAsyncWebview().thenAccept(result -> {

            // Session in Minecraft setzen
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

    public void LoginWithMicrosoft(String email, String password){
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        try {

            MicrosoftAuthResult result = authenticator.loginWithCredentials(email, password);
            System.out.printf("Logged in with '%s'%n", result.getProfile().getName());

        } catch (MicrosoftAuthenticationException e) {
            System.out.println("Failed to Login!!!!!!!: " + e);
        }
    }

}