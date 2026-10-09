package peak.modules.misc;

import net.minecraft.item.Item;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import org.lwjgl.input.Keyboard;

import peak.events.AttackEvent;
import peak.events.PacketEvent;
import peak.events.RenderEvent;
import peak.events.TickEvent;
import peak.managers.PacketManager;
import peak.modules.Module;
import peak.ui.notifications.NotificationManager;


public class TestModule extends Module {


    public TestModule() {
        super("TestModule", Keyboard.KEY_J, Category.MISC, true);
    }

    private int lastUse = 0;

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if(mc.thePlayer.getHealth() < 10 && mc.thePlayer.getAbsorptionAmount() < 3) {
            if(lastUse < 10) return;
            ItemStack[] itemStacks = mc.thePlayer.inventory.mainInventory;
            for(int i = 0; i < 8; i++) {
                if(itemStacks[i] == null) continue;
                if(itemStacks[i].getItem() instanceof ItemSkull) {
                    int originalSlot = mc.thePlayer.inventory.currentItem;
                    PacketManager.sendPacketWithoutEvent(new C09PacketHeldItemChange(i));
                    PacketManager.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(itemStacks[i]));
                    PacketManager.sendPacketWithoutEvent(new C09PacketHeldItemChange(originalSlot));
                    lastUse = 0;
                }
            }
        }
        lastUse++;
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