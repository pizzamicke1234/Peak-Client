package peak.modules.render;

import org.lwjgl.input.Keyboard;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;

public class Animations extends Module {

    public ModeSetting animationmode = new ModeSetting("Mode", false, "1.7","1.7", "Sigma", "Avatar",
            "Slide", "Smooth", "Exhibition", "Liquid");

    public Animations() {
        super("Animations", Keyboard.KEY_NONE, Category.RENDER, true);
        addSetting(animationmode);
    }

}
