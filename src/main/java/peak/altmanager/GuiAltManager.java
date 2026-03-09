package peak.altmanager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
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
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

public class GuiAltManager extends GuiScreen {
    public final ResourceLocation background = new ResourceLocation("peak/backgrounds/menu1.png");
    private GuiTextField nameField, sessionField, addNameField, addSessionField;
    private PeakButton btnExit, btnMicrosoft, btnCracked, btnRandom, btnSession, btnAddCracked, btnAddMicrosoft, btnAddSession;
    private ArrayList<PeakButton> buttons;
    private Random random = new Random();

    private final Color rectColor = new Color(16, 13, 13, 180);

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

        //AccountList
        int addX = width - (width / 5) + 10;
        int addHeight = 20;
        int addWidth = width / 5 - 20;

        this.addNameField = new GuiTextField(0, this.fontRendererObj, addX, 95, addWidth, addHeight);

        btnAddCracked = new PeakButton(5, addX, 120, addWidth, addHeight, "Add Cracked");
        btnAddMicrosoft = new PeakButton(6, addX, 150, addWidth, addHeight, "Add Microsoft");

        this.addSessionField = new GuiTextField(0, this.fontRendererObj, addX, 185, addWidth, addHeight);
        btnAddSession = new PeakButton(7, addX, 210, addWidth, addHeight, "Add Session");

        addSessionField.setMaxStringLength(1500);

        buttons.add(btnCracked);
        buttons.add(btnRandom);
        buttons.add(btnMicrosoft);
        buttons.add(btnExit);
        buttons.add(btnSession);
        buttons.add(btnAddCracked);
        buttons.add(btnAddMicrosoft);
        buttons.add(btnAddSession);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        this.mc.getTextureManager().bindTexture(background);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, this.width, this.height, (float)this.width, (float)this.height);

        drawBackground();

        this.nameField.drawTextBox();
        this.sessionField.drawTextBox();

        this.addNameField.drawTextBox();
        this.addSessionField.drawTextBox();

        for(PeakButton button : buttons) {
            button.drawButton(mouseX, mouseY);
        }

        int x = width / 2 - 115;
        int y = 60;
        int rectWidth = 230;
        int rectHeight = 40;
        int count = 0;
        for(AccountRect accountRect : accountList) {
            accountRect.draw(x, y + ((rectHeight + 5) * count), rectWidth, rectHeight, rectColor);
            count++;
        }

        FontUtil.normal.drawCenteredString("Current Account", this.width / 2, 15, -1);
        FontUtil.normal.drawCenteredString(mc.session.getUsername(), this.width / 2, 30, 0xFF00FF00);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        this.nameField.textboxKeyTyped(typedChar, keyCode);
        this.sessionField.textboxKeyTyped(typedChar, keyCode);

        this.addNameField.textboxKeyTyped(typedChar, keyCode);
        this.addSessionField.textboxKeyTyped(typedChar, keyCode);

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

                    case 5:
                        if (!addNameField.getText().isEmpty()) {
                            Session session = new Session(addNameField.getText(), "0", "0", "legacy");
                            accountList.add(new AccountRect(session));
                        }
                        break;

                    case 6:
                        Session microsoftSession = getLoginWithMicrosoftWeb();
                        if(microsoftSession != null) {
                            accountList.add(new AccountRect(microsoftSession));
                        }
                        break;

                    case 7:
                        Session tokenSession = getLoginWithToken(addSessionField.getText());
                        if(tokenSession != null) {
                            accountList.add(new AccountRect(tokenSession));
                        }
                        break;
                }
            }
        }

        int x = width / 2 - 115;
        int y = 60;
        int rectWidth = 230;
        int rectHeight = 40;
        int count = 0;
        for(AccountRect accountRect : accountList) {
            int offset = (rectHeight + 5) * count;
            if(mouseX >=x && mouseX <= (x + rectWidth) && mouseY >= (y + offset) && mouseY <= (y + rectHeight + offset)) {
                //Holy shit code
                for(AccountRect aR : accountList) {
                    if(aR == accountRect) continue;
                    aR.setSelected(false);
                }
                accountRect.onClick();
            }
            count++;
        }

        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
            this.sessionField.mouseClicked(mouseX, mouseY, mouseButton);

            this.addNameField.mouseClicked(mouseX, mouseY, mouseButton);
            this.addSessionField.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void LoginWithMicrosoftWeb() {
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        Session session;

        authenticator.loginWithAsyncWebview().thenAccept(result -> {

            mc.session = new Session(
                    result.getProfile().getName(),
                    result.getProfile().getId(),
                    result.getAccessToken(),
                    "mojang"
            );

            System.out.println("Login succesful: " + result.getProfile().getName());

        }).exceptionally(ex -> {
            System.out.println("Login Error: " + ex.getMessage());
            return null;
        });
    }

    private Session getLoginWithMicrosoftWeb() {

        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);

        if (cookieManager.getCookieStore() != null) {
            cookieManager.getCookieStore().removeAll();
        }

        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();

        try {

            MicrosoftAuthResult result = authenticator.loginWithAsyncWebview().join();

            String name = result.getProfile().getName();
            String Id = result.getProfile().getId();
            String token = result.getAccessToken();

            return new Session(name, Id, token,"mojang");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    public Session getLoginWithToken(String token) {
        Session session = null;
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

            session = new Session(name, uuid, token, "mojang");

            System.out.println("Succesfully logged in as: " + name);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: Invalid Token");
        }
        return session;
    }

    private String generateRandomUsername() {
        return "Peak" + Math.abs(random.nextInt());
    }

    private void drawBackground() {
        Gui.drawRect(0, 0, width, height, 0x11404040);

        // 0x255f009b
        Gui.drawRect(0, 0, width / 5, height, 0x30555555);
        Gui.drawRect(width - width / 5, 0, width, height, 0x30555555);
        Gui.drawRect(width / 5, 0, width - width / 5, 50, 0x60555555);
    }

}