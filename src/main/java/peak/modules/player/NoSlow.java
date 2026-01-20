package peak.modules.player;

import org.lwjgl.input.Keyboard;
import peak.modules.Module;

public class NoSlow extends Module {

    public NoSlow() {
        super("NoSlow", Keyboard.KEY_NONE, Category.PLAYER, true);
    }

}
