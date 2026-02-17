package peak.modules.movement;

import peak.events.TickEvent;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;
import peak.modules.settings.NumberSetting;

public class Step extends Module {

    ModeSetting stepMode = new ModeSetting("Mode", true, "Vanilla", "Vanilla");
    NumberSetting stepHeight = new NumberSetting("Height", false, 0.5, 2, 0.6, 0.1);

    public Step() {
        super("Step", 0, Category.MOVEMENT, true);
        this.addSetting(stepMode, stepHeight);
    }

    @Override
    public void onDisable() {
        mc.thePlayer.stepHeight = 0.6f;
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {

        if(stepMode.currentValue.equals("Vanilla")) {
            mc.thePlayer.stepHeight = (float)stepHeight.cValue;
        }

    }
}
