package peak.altmanager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import net.minecraft.util.Session;
import java.io.IOException;
import java.util.Optional;

public class GuiAltManager extends GuiScreen {
    private GuiTextField nameField;

    @Override
    public void initGui() {
        // ID, x, y, breite, höhe
        this.nameField = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, 90, "Login (Cracked)"));
        this.nameField.setFocused(true);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) { // Login Button
            if (!nameField.getText().isEmpty()) {
                mc.session = new Session(nameField.getText(), "0", "0", "legacy");
            }
        }

        if (button.id == 2) { // Back Button
            this.mc.displayGuiScreen(new GuiMainMenu());
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.nameField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, "Aktueller Name: " + mc.session.getUsername(), this.width / 2, 30, -1);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        // 1. Textfeld-Eingaben verarbeiten
        this.nameField.textboxKeyTyped(typedChar, keyCode);

        // 2. ESC-Taste prüfen (Keycode 1 ist Escape)
        if (keyCode == 1) {
            this.mc.displayGuiScreen(new GuiMainMenu()); // Oder null, um zum Spiel zurückzukehren
        }

        // Alternativ: super.keyTyped(typedChar, keyCode);
        // Aber bei eigenen Menüs ist die direkte Zuweisung (oben) sicherer.
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        try {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}