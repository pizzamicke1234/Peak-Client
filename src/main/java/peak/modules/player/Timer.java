package peak.modules.player;

import peak.events.TickEvent;
import peak.managers.TimeManager;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;
import peak.modules.settings.NumberSetting;

public class Timer extends Module {

    TimeManager timeManager = new TimeManager();

    private ModeSetting mode = new ModeSetting("Mode", true, "Normal", "Normal", "Balance", "Sinus");

    private NumberSetting timerSpeed = new NumberSetting("Timer", false, 0.1, 10, 1, 0.1);

    //Sinus
    private NumberSetting sinusTimerMin = new NumberSetting("MinTimer", mode, new String[]{"Sinus"} , false, 0.2, 10, 1, 0.2);
    private NumberSetting sinusTime = new NumberSetting("Time", mode, new String[]{"Sinus"} , false, 100, 10000, 1000, 100);

    //Balanced
    private NumberSetting balanceFastTime = new NumberSetting("Fasttime", mode, new String[]{"Balance"}, false , 50, 200, 150, 50);
    private NumberSetting balanceSlowTime = new NumberSetting("Slowtime", mode, new String[]{"Balance"}, false , 50, 200, 150, 50);
    private NumberSetting STimer = new NumberSetting("SlowTimer", mode, new String[]{"Balance"} , false, 0.05, 1, 0.5, 0.05);

    public Timer() {
        super("Timer", 0, Category.PLAYER, true);
        this.addSetting(mode, timerSpeed, sinusTimerMin, sinusTime, STimer, balanceFastTime, balanceSlowTime);
    }

    private int balancedPhase = 0; // 0 = slow, 1 = fast
    private long balancedDelay = 0;

    private float originalTimer;

    @Override
    public void onEnable() {
        originalTimer = mc.timer.timerSpeed;
        balancedPhase = 0;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = originalTimer;
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if(tickType == TickEvent.TickType.POST) return;

        mc.timer.timerSpeed = (float) timerSpeed.cValue;

        switch (mode.currentValue) {
            case "Normal":

                mc.timer.timerSpeed = (float) timerSpeed.cValue;
                break;

            case "Balanced":

                final long   SLOW_MS       = (long) balanceSlowTime.cValue;
                final long   FAST_MS       = (long) balanceFastTime.cValue;
                final float  SLOW_SPEED    = (float) STimer.cValue;
                final float  FAST_SPEED    = (float) timerSpeed.cValue;

                if (balancedPhase < 0 || balancedPhase > 1) {
                    balancedPhase = 0;
                    mc.timer.timerSpeed = SLOW_SPEED;
                    timeManager.reset();
                }

                long activeDuration = (balancedPhase == 0) ? SLOW_MS : FAST_MS;

                if (timeManager.hasReached(activeDuration)) {
                    if (balancedPhase == 0) {
                        balancedPhase = 1;
                        mc.timer.timerSpeed = FAST_SPEED;
                    } else {
                        balancedPhase = 0;
                        mc.timer.timerSpeed = SLOW_SPEED;
                    }

                    timeManager.reset();
                }

                break;

            case "Sinus":
                final double BASE_TIMER_SPEED = timerSpeed.cValue;   // z.B. 1.8–2.5 dein Max-Speed
                final double MIN_TIMER_SPEED = sinusTimerMin.cValue;
                final double AMPLITUDE = (BASE_TIMER_SPEED - MIN_TIMER_SPEED) / 2.0;
                final double OFFSET = MIN_TIMER_SPEED + AMPLITUDE;

                final long CYCLE_TIME_MS = (long) sinusTime.cValue;   // 1.4
                final double FREQUENCY = (2 * Math.PI) / CYCLE_TIME_MS;

                if (balancedPhase < 0 || balancedPhase > 1) {
                    balancedPhase = 0;
                    timeManager.reset();
                }

                long elapsed = timeManager.getElapsedTime();

                double sinValue = Math.sin(elapsed * FREQUENCY);
                float currentSpeed = (float) (OFFSET + AMPLITUDE * sinValue);

                currentSpeed = Math.max(0.05f, Math.min(5.0f, currentSpeed));

                mc.timer.timerSpeed = currentSpeed;

                balancedPhase = (sinValue > 0) ? 1 : 0;

                break;

        }
    }
}