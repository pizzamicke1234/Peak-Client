package peak.modules.misc;

import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import peak.managers.NotificationManager;
import peak.managers.PacketManager;
import peak.modules.Module;
import peak.modules.settings.BoolSetting;
import peak.modules.settings.ModeSetting;
import peak.tickevents.TickEvent;

public class TestModule extends Module {

    ModeSetting Mode = new ModeSetting("Mode", true, "Test", "Test", "Test1", "Test2", "Test3");
    ModeSetting Mode1 = new ModeSetting("Mode1", true, "Test", "Test", "Test1", "Test2", "Test3");
    BoolSetting PacketListener = new BoolSetting("PacketListener", true, false);

    public TestModule() {
        super("TestModule", Keyboard.KEY_J, Category.MISC, true);
        addSetting(Mode, Mode1, PacketListener);
    }

    @Override
    public void on_Enable() {
        NotificationManager.addChat("Enabled Test Module!");
        PacketManager.sendPacket(new C02PacketUseEntity());
    }

    @Override
    public void on_Disable() {
        NotificationManager.addChat("Disabled Test Module!");
        PacketManager.uncancelPacketType(C0FPacketConfirmTransaction.class);
    }

    @Override
    public void on_Tick(TickEvent.TickType tickType) {
        if(tickType == TickEvent.TickType.POST) return;

        ItemStack heldStack = mc.thePlayer.getHeldItem();

        if(heldStack == null) {
            NotificationManager.addChat("Held Item | null");
            return;
        }

        NotificationManager.addChat("Held Item | " + mc.thePlayer.getHeldItem().getItem());

    }

}
