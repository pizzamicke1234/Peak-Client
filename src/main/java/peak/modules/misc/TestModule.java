package peak.modules.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import org.lwjgl.input.Keyboard;

import peak.events.PacketEvent;
import peak.events.TickEvent;
import peak.modules.Module;
import peak.ui.notifications.NotificationManager;

import java.awt.*;


public class TestModule extends Module {


    public TestModule() {
        super("TestModule", Keyboard.KEY_J, Category.MISC, true);
    }

    @Override
    public void onEnable() {
        for(int i = 0; i < 5; i++) {
            NotificationManager.addChat("Number = " + i);
        }
    }
}