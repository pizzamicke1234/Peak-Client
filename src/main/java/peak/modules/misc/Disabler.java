package peak.modules.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import org.lwjgl.input.Keyboard;
import peak.events.PacketEvent;
import peak.managers.PacketManager;
import peak.managers.TimeManager;
import peak.managers.misc.BlinkManager;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;
import peak.events.TickEvent;

import java.util.ArrayList;

public class Disabler extends Module {

    ModeSetting disablermode = new ModeSetting("Mode", true, "Deathzone Movement", "Deathzone Movement", "Test");

    public Disabler() {
        super("Disabler", Keyboard.KEY_R, Category.MISC, true);
        addSetting(disablermode);
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

        if(disablermode.currentValue.equals("Deathzone Movement")) {

            PacketManager.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            PacketManager.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));

            if (timer.hasReached((long) (5000 + (Math.random() * 1000)))) {
                packetList.forEach(PacketManager::sendPacketWithoutEvent);
                packetList.clear();
                timer.reset();
            }

            BlinkManager.blinking = true;
            if (mc.thePlayer.ticksExisted % 2 == 0) {
                BlinkManager.dispatch();
            }

        }

    }

    @Override
    public void onPacket(PacketEvent packetEvent) {

        if(disablermode.currentValue.equals("Deathzone Movement")) {

            if(packetEvent.getPacket() instanceof C17PacketCustomPayload) {
                packetEvent.cancelPacket();
            }

            if(packetEvent.getPacket() instanceof C0FPacketConfirmTransaction) {
                packetList.add(packetEvent.getPacket());
                packetEvent.cancelPacket();
            }
        }

    }

}
