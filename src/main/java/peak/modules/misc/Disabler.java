package peak.modules.misc;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import org.lwjgl.input.Keyboard;
import peak.events.PacketEvent;
import peak.managers.NotificationManager;
import peak.managers.PacketManager;
import peak.managers.TimeManager;
import peak.modules.Module;
import peak.modules.settings.BoolSetting;
import peak.modules.settings.ModeSetting;
import peak.events.TickEvent;

import java.util.ArrayList;
import java.util.List;

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

        if(disablermode.current_value == "Vulcan Combat") {

            if(packetEvent.getPacket() instanceof C0FPacketConfirmTransaction) {
                packetList.add(packetEvent.getPacket());
                packetEvent.cancelPacket();
            }

            if (timer.hasReached((long) (2000 + (Math.random() * 1000)))) {
                packetList.forEach(PacketManager::sendPacketWithoutEvent);
                packetList.clear();
                timer.reset();
            }

        }

    }

}
