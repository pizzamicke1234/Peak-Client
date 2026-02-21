package peak.modules.movement;

import peak.events.TickEvent;
import peak.modules.Module;

public class Spider extends Module {

    public Spider() {
        super("Spider", 0, Category.MOVEMENT, true);
    }


    @Override
    public void onDisable() {

    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if (tickType == TickEvent.TickType.POST) return;

        if (mc.thePlayer.isCollidedHorizontally) {
            mc.thePlayer.motionY = 0.3F;
        }
    }
}
