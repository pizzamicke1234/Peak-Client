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
        jumpCount = 0;
        if(mc.thePlayer.onGround) {
            startY = mc.thePlayer.posY;
            mc.thePlayer.jump();
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

        if(mc.thePlayer.posY < startY && ticks > 15) {
            MovementManager.strafe(0.3f);
            mc.thePlayer.onGround = true;
            PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                    mc.thePlayer.posY, mc.thePlayer.posZ, true));
            PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX,
                    mc.thePlayer.posY - 0.3D, mc.thePlayer.posZ, false));

            mc.thePlayer.jump();
            ticks = 0;
            jumpCount++;
            if(jumpCount >= 3) {
                this.toggle();
            }
        }

        ticks++;

    }

    @Override
    public void onPacket(PacketEvent packetEvent) {
        if(mc.thePlayer.posY > startY) {
            if(packetEvent.getPacket() instanceof C0FPacketConfirmTransaction) {
                packetEvent.cancelPacket();
            }
        }
    }
}
