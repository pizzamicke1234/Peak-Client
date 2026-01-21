package peak.modules.player;

import org.lwjgl.input.Keyboard;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;

public class NoSlow extends Module {

    ModeSetting noslowmode = new ModeSetting("NoSlowmode", "Vanilla", true, "Vanilla");

    public NoSlow() {
        super("NoSlow", Keyboard.KEY_NONE, Category.PLAYER, true);
        addSetting(noslowmode);
    }

}
