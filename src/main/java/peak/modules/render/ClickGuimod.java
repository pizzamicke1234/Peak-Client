package peak.modules.render;

import org.lwjgl.input.Keyboard;
import peak.modules.Module;

import java.security.Key;

public class ClickGuimod extends Module {

    public ClickGuimod() {
        super("ClickGui", Keyboard.KEY_RSHIFT, Category.RENDER);
    }

    public void on_Enable() {
        
    }

    public void on_Disable() {

    }

}
