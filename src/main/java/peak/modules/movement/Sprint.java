package peak.modules.movement;

import org.lwjgl.input.Keyboard;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;


public class Sprint extends Module {

    ModeSetting sprintmode = new ModeSetting("Mode",true,  "Legit", "Legit", "Omnisprint");

    public Sprint() {
        super("Sprint", Keyboard.KEY_NONE, Category.MOVEMENT, true);
        addSetting(sprintmode);
        toggled = true;
    }

    public void on_Enable() {

    }

    public void on_Disable() {

    }

    public void on_Tick() {

        switch (sprintmode.current_value) {
            case "Legit":
                if(mc.gameSettings.keyBindForward.isKeyDown()) {
                    mc.thePlayer.setSprinting(true);
                }
                break;
            case "Omnisprint":
                mc.thePlayer.setSprinting(true);
                break;
        }
    }

}
