package peak.modules.misc;

import org.lwjgl.input.Keyboard;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;

public class Phase extends Module {

    ModeSetting phaseMode = new ModeSetting("Mode", false, "Clip", "Clip");

    public Phase() {
        super("Phase", Keyboard.KEY_U, Category.MISC, true);
        addSetting(phaseMode);
    }

    @Override
    public void onEnable() {

        if(phaseMode.current_value == "Clip") {
            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 3, mc.thePlayer.posZ);
            this.toggle();
        }

    }

}
