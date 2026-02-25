package peak.modules.player;

import peak.events.TickEvent;
import peak.modules.Module;
import peak.modules.settings.NumberSetting;

public class Timer extends Module {

    private NumberSetting timerSpeed = new NumberSetting("Timer", false, 0.1, 10, 1, 0.1);

    public Timer() {
        super("Timer", 0, Category.PLAYER, true);
        this.addSetting(timerSpeed);
    }

    float originalTimer;

    @Override
    public void onEnable() {
        originalTimer = mc.timer.timerSpeed;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = originalTimer;
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if(tickType == TickEvent.TickType.PRE) {
            mc.timer.timerSpeed = (float) timerSpeed.cValue;
        }
    }
}
