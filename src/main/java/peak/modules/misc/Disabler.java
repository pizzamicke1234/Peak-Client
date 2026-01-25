package peak.modules.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import org.lwjgl.input.Keyboard;
import peak.managers.NotificationManager;
import peak.managers.PacketManager;
import peak.modules.Module;
import peak.modules.settings.BoolSetting;
import peak.modules.settings.ModeSetting;
import peak.tickevents.TickEvent;

public class Disabler extends Module {

    ModeSetting disablermode = new ModeSetting("Mode", true, "Test", "Test");

    public BoolSetting debug = new BoolSetting("Debug", false, false);

    public Disabler() {
        super("Disabler", Keyboard.KEY_R, Category.MISC, true);
        addSetting(disablermode, debug);
    }

    @Override
    public void on_Enable() {

    }

    @Override
    public void on_Disable() {

    }

    @Override
    public void on_Tick(TickEvent.TickType tickType) {
        if(tickType == TickEvent.TickType.POST) return;

    }

}
