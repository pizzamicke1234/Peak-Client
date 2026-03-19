package peak.modules.render;

import org.lwjgl.input.Keyboard;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;
import peak.modules.settings.NumberSetting;

public class Animations extends Module {

    public ModeSetting animationmode = new ModeSetting("Mode", false, "1.7","1.7", "Sigma", "Avatar",
            "Slide", "Smooth", "Exhibition", "Liquid", "ETB");

    public NumberSetting blockX = new NumberSetting("Block X", false, -50, 100, 0, 5);
    public NumberSetting blockY = new NumberSetting("Block Y", false, -50, 100, 0, 5);
    public NumberSetting blockZ = new NumberSetting("Block Z", false, -50, 100, 0, 5);

    public NumberSetting swingSpeed = new NumberSetting("Swing Speed", false, 0.1, 3, 1,0.1);

    public Animations() {
        super("Animations", Keyboard.KEY_NONE, Category.RENDER, true);
        addSetting(animationmode, blockX, blockY, blockZ, swingSpeed);
    }

}
