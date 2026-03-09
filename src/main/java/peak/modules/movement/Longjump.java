package peak.modules.movement;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import org.lwjgl.input.Keyboard;
import peak.events.PacketEvent;
import peak.events.TickEvent;
import peak.managers.MovementManager;
import peak.managers.PacketManager;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;

public class Longjump extends Module {

    ModeSetting mode = new ModeSetting("Mode", true, "Test", "Test");

    public Longjump() {
        super("Longjump", Keyboard.KEY_N, Category.MOVEMENT, true);
        this.addSetting(mode);
    }

    int jumpCount = 0;
    int ticks = 0;
    double startY ;

    @Override
    public void onEnable() {
        ticks = 0;
        if(mc.thePlayer.onGround) {
            mc.thePlayer.motionY = 1f;
            MovementManager.strafe(1.5f);
        }
        else {
            this.toggle();
        }
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {

        if(mc.thePlayer.onGround && ticks > 3) {
            this.toggle();
        }

        MovementManager.strafe(0.8f);

        ticks++;

    }
}
