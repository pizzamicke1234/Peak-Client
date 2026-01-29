package peak.modules.movement;

import org.lwjgl.input.Keyboard;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;
import peak.events.TickEvent;


public class Sprint extends Module {

    ModeSetting sprintmode = new ModeSetting("Mode",false,  "Legit", "Legit", "Omnisprint");

    public Sprint() {
        super("Sprint", Keyboard.KEY_NONE, Category.MOVEMENT, true);
        addSetting(sprintmode);
        toggled = true;
    }

    public void onEnable() {

    }

    public void onDisable() {

    }

    public void onTick(TickEvent.TickType tickType) {

        if(tickType == TickEvent.TickType.POST) return;

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
