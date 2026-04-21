package peak.modules.misc;

import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1EPacketRemoveEntityEffect;
import org.lwjgl.input.Keyboard;

import peak.events.PacketEvent;
import peak.events.RenderEvent;
import peak.events.TickEvent;
import peak.managers.render.RenderManager;
import peak.modules.Module;
import peak.ui.notifications.NotificationManager;

import java.awt.*;


public class TestModule extends Module {


    public TestModule() {
        super("TestModule", Keyboard.KEY_J, Category.MISC, true);
    }

    @Override
    public void onRender(RenderEvent renderEvent) {
        RenderManager.drawSelectionBox(0, 2, 0, 1, 2, new Color(0, 255, 57, 255));
    }
}