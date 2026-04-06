package peak.modules.render;

import org.lwjgl.input.Keyboard;
import peak.Client;
import peak.modules.Module;
import peak.ui.clickguis.classic.ClickGui;
import peak.ui.clickguis.classic.elements.CategoryRect;
import peak.ui.clickguis.classic.elements.ModuleRect;
import peak.ui.clickguis.modern.ClickGuiModern;

import java.util.ArrayList;

public class ClickGuimod extends Module {

    public ClickGuimod() {
        super("ClickGui", Keyboard.KEY_RSHIFT, Category.RENDER, false);
    }

    private HUDMod hudMod = (HUDMod) Client.getModulebyName("HUD");

    //Used to save the Position of classic ClickGui elements when closed
    public ArrayList<CategoryRect> categoryRects = new ArrayList<>();
    public ArrayList<ModuleRect> moduleRects = new ArrayList<>();

    @Override
    public void onEnable() {
        if(hudMod == null) {
            hudMod = (HUDMod) Client.getModulebyName("HUD");
        }
        switch (hudMod.clickGuiStyle.currentValue) {
            case "Default":
                mc.displayGuiScreen(new ClickGui());
                break;

            case "New":
                mc.displayGuiScreen(new ClickGuiModern());
                break;
        }
    }

    @Override
    public void onDisable() {
        mc.displayGuiScreen(null);
    }

}
