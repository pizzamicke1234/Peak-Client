package peak.modules.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.world.WorldProvider;
import org.lwjgl.input.Keyboard;
import peak.managers.DamageManager;
import peak.managers.PacketManager;
import peak.managers.render.HitBox;
import peak.managers.render.RenderManager;
import peak.modules.Module;
import peak.events.TickEvent;
import peak.ui.notifications.Notification;
import peak.ui.notifications.NotificationManager;

public class TestModule extends Module {

    public TestModule() {
        super("TestModule", Keyboard.KEY_J, Category.MISC, true);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if(tickType == TickEvent.TickType.POST) return;

        mc.thePlayer.motionY = 0;

        /*if(mc.thePlayer.ticksExisted % 1 == 0) {
            PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + 1, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
            mc.thePlayer.setPosition(mc.thePlayer.posX + 1, mc.thePlayer.posY, mc.thePlayer.posZ);
        }*/

    }
}
