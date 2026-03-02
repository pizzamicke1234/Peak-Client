package peak.modules.fun;

import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import peak.managers.PacketManager;
import peak.modules.Module;
import peak.ui.notifications.NotificationManager;

public class DeathzoneFunny extends Module {

    public DeathzoneFunny() {
        super("DeathzoneFunny", 0, Category.FUN, true);
    }

    private int ticks = 0;

    @Override
    public void onEnable() {
        ticks = 0;
        dotheFunny();
        mc.thePlayer.motionY = 1;
        mc.thePlayer.motionZ = 3;
        NotificationManager.addChat("Did the Funny");
        this.toggle();
    }

    private void dotheFunny() {
        ItemStack itemStack = mc.thePlayer.inventory.mainInventory[8];

        PacketManager.sendPacketWithoutEvent(new C09PacketHeldItemChange(8));
        PacketManager.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(itemStack));

    }
}
