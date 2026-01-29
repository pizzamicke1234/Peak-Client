package peak.modules.render;

import org.lwjgl.input.Keyboard;
import peak.modules.Module;
import peak.ui.clickguis.ClickGui;

import java.security.Key;

public class ClickGuimod extends Module {

    public ClickGuimod() {
        super("ClickGui", Keyboard.KEY_RSHIFT, Category.RENDER, false);
    }

    @Override
    public void onEnable() {
        mc.displayGuiScreen(new ClickGui());
    }

    @Override
    public void onDisable() {
        mc.displayGuiScreen(null);
    }

}
