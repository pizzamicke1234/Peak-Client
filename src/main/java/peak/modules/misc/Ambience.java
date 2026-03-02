package peak.modules.misc;

import net.minecraft.network.play.server.S03PacketTimeUpdate;
import peak.events.PacketEvent;
import peak.events.TickEvent;
import peak.modules.Module;
import peak.modules.settings.BoolSetting;
import peak.modules.settings.NumberSetting;

public class Ambience extends Module {

    NumberSetting worldTime = new NumberSetting("Time", false, 1, 16000, 6000, 1000);
    BoolSetting fullbright = new BoolSetting("Fullbright", false, true);

    public Ambience() {
        super("Ambience", 0, Category.MISC, true);
        this.addSetting(worldTime, fullbright);
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        mc.theWorld.setWorldTime((long) worldTime.cValue);
        if(fullbright.isTrue()) {
            mc.gameSettings.gammaSetting = 1000L;
        }
    }

    @Override
    public void onPacket(PacketEvent packetEvent) {

        if(packetEvent.getPacket() instanceof S03PacketTimeUpdate) {
            packetEvent.cancelPacket();
        }

    }
}
