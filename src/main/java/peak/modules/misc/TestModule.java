package peak.modules.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;
import peak.events.PacketEvent;
import peak.events.TickEvent;
import peak.managers.PacketManager;
import peak.modules.Module;
import peak.ui.notifications.NotificationManager;

public class TestModule extends Module {


    public TestModule() {
        super("TestModule", Keyboard.KEY_J, Category.MISC, true);
    }

    private int ticks = 0;

    @Override
    public void onEnable() {
        ticks = 0;
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if (tickType == TickEvent.TickType.PRE) {

            ticks++;

        }
    }

    @Override
    public void onPacket(PacketEvent packetEvent) {

        Packet packet = packetEvent.getPacket();

        if(packet instanceof C03PacketPlayer) {

            NotificationManager.addChat("C");
            packetEvent.cancelPacket();

            if(ticks > 60) {
                //PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(0, 0, 0, true));
            }

        }

    }
}