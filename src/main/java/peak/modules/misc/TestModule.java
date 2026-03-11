package peak.modules.misc;

import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Keyboard;
import peak.events.PacketEvent;
import peak.modules.Module;
import peak.ui.notifications.NotificationManager;

public class TestModule extends Module {


    public TestModule() {
        super("TestModule", Keyboard.KEY_J, Category.MISC, true);
    }

    @Override
    public void onPacket(PacketEvent packetEvent) {

        NotificationManager.addChat("New Packet: " + packetEvent.getPacket().getClass().getSimpleName());

    }
}