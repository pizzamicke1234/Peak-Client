package peak.modules.misc;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import org.lwjgl.input.Keyboard;
import peak.events.PacketEvent;
import peak.managers.DamageManager;
import peak.managers.NotificationManager;
import peak.managers.PacketManager;
import peak.modules.Module;
import peak.events.TickEvent;

public class TestModule extends Module {

    public TestModule() {
        super("TestModule", Keyboard.KEY_J, Category.MISC, true);
    }

    @Override
    public void onEnable() {
        NotificationManager.addChat("Enabled Test Module!");
    }

    @Override
    public void onDisable() {
        NotificationManager.addChat("Disabled Test Module!");
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if(tickType == TickEvent.TickType.POST) return;

        if(mc.gameSettings.keyBindJump.isPressed()) {
            PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
        }

    }

    @Override
    public void onPacket(PacketEvent packetEvent) {

        if(packetEvent.getPacket() instanceof C03PacketPlayer) {
            packetEvent.cancelPacket();
        }

    }

}
