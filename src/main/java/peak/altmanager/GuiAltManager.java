package peak.altmanager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import peak.altmanager.auth.AccountRect;
import peak.managers.font.FontUtil;
import peak.ui.mainmenus.PeakMainMenu;
import peak.ui.mainmenus.elements.PeakButton;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class GuiAltManager extends GuiScreen {
    public final ResourceLocation background = new ResourceLocation("peak/backgrounds/menu1.png");
    private GuiTextField nameField, sessionField;
    private PeakButton btnExit, btnMicrosoft, btnCracked, btnRandom, btnSession;
    private ArrayList<PeakButton> buttons;
    private Random random = new Random();

    private ArrayList<AccountRect> accountList = new ArrayList<>();

    @Override
    public void initGui() {
        buttons = new ArrayList<>();

        this.nameField = new GuiTextField(0, this.fontRendererObj, 10, 95, width / 5 - 20, 20);

        btnCracked = new PeakButton(0, 10, 120, width / 5 - 20, 20, "Cracked Login");
        btnRandom = new PeakButton(3, 10, 145, width / 5 - 20, 20, "Random Cracked");
        btnMicrosoft = new PeakButton(1, 10, 175, width / 5 - 20, 20, "Microsoft Login");
        btnExit = new PeakButton(2, this.width / 2 - 80, this.height - 40, 160,
                20, "Close");

        this.sessionField = new GuiTextField(0, this.fontRendererObj, 10, 210, width / 5 - 20, 20);
        btnSession = new PeakButton(4, 10, 235, width / 5 - 20, 20, "Session Login");
        sessionField.setMaxStringLength(1500);

        buttons.add(btnCracked);
        buttons.add(btnRandom);
        buttons.add(btnMicrosoft);
        buttons.add(btnExit);
        buttons.add(btnSession);

        Session testSession = new Session("TestPlayer", "0", "0", "legacy");
        Color rectColor = new Color(255, 255, 255, 197);
        AccountRect testAccount = new AccountRect(testSession, width / 2 - 150, 200, 300, 40, rectColor);
        accountList.add(testAccount);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        this.mc.getTextureManager().bindTexture(background);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, (float)this.width, (float)this.height);

        drawBackground();

        this.nameField.drawTextBox();
        this.sessionField.drawTextBox();

        for(PeakButton button : buttons) {
            button.drawButton(mouseX, mouseY);
        }

        for(AccountRect accountRect : accountList) {
            accountRect.draw();
        }

        FontUtil.normal.drawCenteredString("Current Account", this.width / 2, 15, -1);
        FontUtil.normal.drawCenteredString(mc.session.getUsername(), this.width / 2, 30, 0xFF00FF00);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        this.nameField.textboxKeyTyped(typedChar, keyCode);
        this.sessionField.textboxKeyTyped(typedChar, keyCode);

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

                    case 3:
                        mc.session = new Session(generateRandomUsername(), "0", "0", "legacy");
                        break;

                    case 4:
                        loginWithToken(sessionField.getText());
                        break;
                }
            }
        }

        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
            this.sessionField.mouseClicked(mouseX, mouseY, mouseButton);
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

    public void loginWithToken(String token) {
        try {

            URL url = new URL("https://api.minecraftservices.com/minecraft/profile");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) response.append(line);
            reader.close();

            JsonObject json = new JsonParser().parse(response.toString()).getAsJsonObject();
            String uuid = json.get("id").getAsString();
            String name = json.get("name").getAsString();

            Minecraft.getMinecraft().session = new Session(name, uuid, token, "mojang");

            System.out.println("Succesfully logged in as: " + name);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Invalid Token");
        }
    }

    private String generateRandomUsername() {
        return "Peak" + Math.abs(random.nextInt());
    }

    private void drawBackground() {
        Gui.drawRect(0, 0, width, height, 0x11404040);

        // 0x255f009b

        Gui.drawRect(0, 0, width / 5, height, 0x30555555);
        Gui.drawRect(width - width / 5, 0, width, height, 0x30555555);
    }

}