package peak.modules.render;

import org.lwjgl.input.Keyboard;
import peak.modules.Module;
import peak.ui.clickguis.ClickGui;
import peak.ui.clickguis.elements.CategoryRect;
import peak.ui.clickguis.elements.ModuleRect;

import java.security.Key;
import java.util.ArrayList;

public class ClickGuimod extends Module {

    public ClickGuimod() {
        super("ClickGui", Keyboard.KEY_RSHIFT, Category.RENDER, false);
    }

    //Used to save the Position of ClickGui elements when closed
    public ArrayList<CategoryRect> categoryRects = new ArrayList<>();
    public ArrayList<ModuleRect> moduleRects = new ArrayList<>();

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new ClickGui());
    }

    @Override
    public void onDisable() {
        mc.displayGuiScreen(null);
    }

}
