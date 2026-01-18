package peak.modules.movement;

import org.lwjgl.input.Keyboard;
import peak.modules.Module;

public class Fly extends Module {

    public Fly() {
        super("Fly", Keyboard.KEY_Y, Category.MOVEMENT);
    }

    public void on_Enable() {
        mc.thePlayer.capabilities.allowFlying = true;
    }

    public void on_Disable() {
        mc.thePlayer.capabilities.isFlying = false;
        mc.thePlayer.capabilities.allowFlying = false;
    }

    public void on_Tick() {
        mc.thePlayer.capabilities.isFlying = true;
    }

}
