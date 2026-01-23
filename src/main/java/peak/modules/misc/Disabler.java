package peak.modules.misc;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.*;
import org.lwjgl.input.Keyboard;
import peak.managers.NotificationManager;
import peak.managers.PacketManager;
import peak.modules.Module;
import peak.modules.settings.BoolSetting;
import peak.modules.settings.ModeSetting;
import peak.tickevents.TickEvent;

public class Disabler extends Module {

    ModeSetting disablermode = new ModeSetting("Mode", true, "Vulcan Experimental",
            "Vulcan Experimental", "Test");

    public BoolSetting debug = new BoolSetting("Debug", false, false);

    public Disabler() {
        super("Disabler", Keyboard.KEY_R, Category.MISC, true);
        addSetting(disablermode, debug);
    }

    private int c03Count = 0;
    private Entity forcedVehicle = null;

    @Override
    public void on_Enable() {

        if (mc.thePlayer.ridingEntity != null) {
            forcedVehicle = mc.thePlayer.ridingEntity;

            mc.thePlayer.ridingEntity = null;

            mc.thePlayer.posY += 1;

            NotificationManager.addChat("Boat Spoofing");
        } else {
            NotificationManager.addChat("§cGet in a boat to enable Disabler!");
            this.toggle();
        }

    }

    @Override
    public void on_Disable() {
        mc.timer.timerSpeed = 1.0f;
        c03Count = 0;

        PacketManager.releasePackets();

        if (forcedVehicle != null) {
            mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
            forcedVehicle = null;
        }
        //NotificationManager.addChat("§cSucces!");
    }

    @Override
    public void on_Tick(TickEvent.TickType tickType) {
        if(tickType == TickEvent.TickType.POST) return;

        switch (disablermode.current_value) {

            case "Vulcan Experimental":
                Vulcan();
                break;

            case "Test":
                PacketManager.cancelTransactions = debug.isTrue();

                if (mc.thePlayer.ridingEntity != null) {
                    mc.timer.timerSpeed = 0.5f;
                }
                break;

        }

    }

    public void Vulcan() {

        if (mc.thePlayer != null) {
            mc.thePlayer.motionY = 0;
            //mc.timer.timerSpeed = 1.3f;
        }

    }

    public boolean handleBoatFly(Packet<?> packet) {

        if (packet instanceof C03PacketPlayer) {
            c03Count++;

            if (c03Count >= 10) {
                c03Count = 0;
                PacketManager.blinkBuffer.add(packet);
                PacketManager.releasePackets();
                return true;
            }

            return true;
        }

        if (packet instanceof C0FPacketConfirmTransaction || packet instanceof C00PacketKeepAlive) {
            PacketManager.blinkBuffer.add(packet);
            return true;
        }

        return false;
    }

}
