package peak.modules.misc;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import peak.events.AttackEvent;
import peak.events.PacketEvent;
import peak.events.RenderEvent;
import peak.events.TickEvent;
import peak.modules.Module;
import peak.ui.notifications.NotificationManager;


public class TestModule extends Module {


    public TestModule() {
        super("TestModule", Keyboard.KEY_J, Category.MISC, true);
    }

    @Override
    public void onEnable() {
        Item item = mc.thePlayer.getHeldItem().getItem();
        NotificationManager.addChat("Item: " + item.getUnlocalizedName());
    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onTick(TickEvent.TickType tickType) {

    }

    @Override
    public void onPacket(PacketEvent packetEvent) {

    }

    @Override
    public void onRender(RenderEvent renderEvent) {

    }

    @Override
    public void onAttack(AttackEvent attackEvent) {

    }
}