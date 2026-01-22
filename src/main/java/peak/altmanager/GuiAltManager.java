package peak.altmanager;

import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.model.AuthAgent;
import fr.litarvan.openauth.model.response.AuthResponse;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;

import net.minecraft.util.Session;
import org.lwjgl.Sys;
import peak.ui.mainmenus.PeakMainMenu;
import peak.ui.mainmenus.elements.PeakButton;

import java.io.IOException;

public class GuiAltManager extends GuiScreen {
    private GuiTextField nameField, pwField;
    public PeakButton btnExit, btnmicrosoft;

    @Override
    public void initGui() {
        // ID, x, y, breite, höhe
        this.nameField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
        this.pwField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 90, 200, 20);

        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 120, "Cracked Login"));

        btnmicrosoft = new PeakButton(3, this.width / 2 - 100, 150, this.width / 2 + 100, 170, "Microsoft Login");
        btnExit = new PeakButton(2, this.width / 2 - 80, this.height - 40, this.width / 2 + 80,
                this.height - 20, "Close");

        this.nameField.setFocused(true);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) { // Login Button
            if (!nameField.getText().isEmpty()) {
                mc.session = new Session(nameField.getText(), "0", "0", "legacy");
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.nameField.drawTextBox();
        this.pwField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
        btnExit.drawButton(mouseX, mouseY);
        btnmicrosoft.drawButton(mouseX, mouseY);
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

        if(btnmicrosoft.isClicked(mouseX, mouseY)) {
            LoginWithMicrosoftWeb();
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

            System.out.println("Login erfolgreich: " + result.getProfile().getName());

        }).exceptionally(ex -> {
            System.out.println("Login abgebrochen oder Fehler: " + ex.getMessage());
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