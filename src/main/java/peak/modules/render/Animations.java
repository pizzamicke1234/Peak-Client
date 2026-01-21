package peak.modules.render;

import org.lwjgl.input.Keyboard;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;

public class Animations extends Module {

    public ModeSetting animationmode = new ModeSetting("animationmode", "1.7", true, "1.7");

    public Animations() {
        super("Animations", Keyboard.KEY_NONE, Category.RENDER, true);
        addSetting(animationmode);
    }

}
