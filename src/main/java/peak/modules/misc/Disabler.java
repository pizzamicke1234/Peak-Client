package peak.modules.misc;

import net.minecraft.network.Packet;
import org.lwjgl.input.Keyboard;
import peak.events.PacketEvent;
import peak.managers.TimeManager;
import peak.modules.Module;
import peak.modules.settings.BoolSetting;
import peak.modules.settings.ModeSetting;
import peak.events.TickEvent;

import java.util.ArrayList;

public class Disabler extends Module {

    ModeSetting disablermode = new ModeSetting("Mode", true, "Deathzone Airlines", "Deathzone Airlines", "Vulcan Combat");

    public BoolSetting debug = new BoolSetting("Debug", false, false);

    public Disabler() {
        super("Disabler", Keyboard.KEY_R, Category.MISC, true);
        addSetting(disablermode, debug);
    }

    ArrayList<Packet> packetList = new ArrayList<>();
    TimeManager timer = new TimeManager();

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if(tickType == TickEvent.TickType.POST) return;

    }

    @Override
    public void onPacket(PacketEvent packetEvent) {

        if(disablermode.currentValue.equals("Deathzone Airlines")) {


        }

    }

}
