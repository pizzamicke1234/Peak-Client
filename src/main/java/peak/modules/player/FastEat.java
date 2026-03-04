package peak.modules.player;

import net.minecraft.network.play.client.C03PacketPlayer;
import peak.events.PacketEvent;
import peak.events.TickEvent;
import peak.managers.PacketManager;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;
import peak.modules.settings.NumberSetting;

public class FastEat extends Module {

    private ModeSetting mode = new ModeSetting("Mode", true, "Instant", "Instant", "Timer", "Deathzone");
    private NumberSetting timerSetting = new NumberSetting("Timer", mode, new String[]{"Timer"}, false, 1,
            10, 1, 0.1D);

    public FastEat() {
        super("FastEat", 0, Category.PLAYER, true);
        this.addSetting(mode, timerSetting);
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {

        if(mc.thePlayer.isEating()) {
            switch (mode.currentValue) {

                case "Instant":
                    for(int i = 0; i < 35; i++) {
                        PacketManager.sendPacket(new C03PacketPlayer(mc.thePlayer.onGround));
                    }
                    break;

                case "Timer":
                    mc.timer.timerSpeed = (float) timerSetting.cValue;
                    break;

                case "Deathzone":
                    mc.timer.timerSpeed = 7f;
                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionY *= (1D/7D);
                    mc.thePlayer.motionZ = 0;
                    break;

            }
        }else {
            if(mode.currentValue.equals("Timer")) {
                if(mc.timer.timerSpeed == (float) timerSetting.cValue) {
                    mc.timer.timerSpeed = 1;
                }
            }

            if(mode.currentValue.equals("Deathzone")) {
                if(mc.timer.timerSpeed == 7f) {
                    mc.timer.timerSpeed = 1;
                }
            }

        }

    }

    @Override
    public void onPacket(PacketEvent packetEvent) {
        super.onPacket(packetEvent);
    }
}
