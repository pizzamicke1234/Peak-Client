package peak.modules.player;

import peak.events.TickEvent;
import peak.managers.misc.BlinkManager;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;

public class Blink extends Module {

    ModeSetting blinkMode = new ModeSetting("Mode", false, "Normal", "Normal", "Pulse");

    public Blink() {
        super("Blink", 0, Category.PLAYER, true);
        this.addSetting(blinkMode);
    }

    @Override
    public void onEnable() {
        BlinkManager.blinking = true;
    }

    @Override
    public void onDisable() {
        BlinkManager.dispatch();
        BlinkManager.blinking = false;
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {

        BlinkManager.blinking = true;

    }
}
