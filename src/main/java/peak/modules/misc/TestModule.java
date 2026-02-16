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
        mc.thePlayer.inventory.currentItem = 2;
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if(tickType == TickEvent.TickType.POST) return;

       System.out.println(mc.thePlayer.getActivePotionEffects());

    }
}
