package peak.modules.movement;

import org.lwjgl.input.Keyboard;
import peak.modules.Module;


public class Sprint extends Module {

    public Sprint() {
        super("Sprint", Keyboard.KEY_NONE, Category.MOVEMENT);

        toggled = true;
    }

    public void on_Enable() {

    }

    public void on_Disable() {

    }

    public void on_Tick() {
        if(mc.gameSettings.keyBindForward.isKeyDown()) {
            mc.thePlayer.setSprinting(true);
        }
    }

}
