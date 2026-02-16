package peak.modules.player;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.potion.PotionEffect;
import peak.Client;
import peak.events.TickEvent;
import peak.managers.PacketManager;
import peak.modules.Module;
import peak.modules.combat.Killaura;

import java.util.Collection;
import java.util.List;

public class AutoPotion extends Module {

    Killaura killaura = (Killaura) Client.getModulebyName("Killaura");
    Scaffold scaffold = (Scaffold) Client.getModulebyName("Scaffold");

    public AutoPotion() {
        super("AutoPotion", 0, Category.PLAYER, true);
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if(tickType == TickEvent.TickType.POST) return;
        //Some delay for autopot
        if(!(mc.thePlayer.ticksExisted % 10 == 0)) return;

        for(int i = 0; i < 9; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
            if(itemStack != null && itemStack.getItem() instanceof ItemPotion) {

                List<PotionEffect> potionEffect = ((ItemPotion) itemStack.getItem()).getEffects(itemStack);
                String effectName = potionEffect.get(0).getEffectName();

                if(effectName.equalsIgnoreCase("potion.damageBoost") || effectName.equalsIgnoreCase("potion.moveSpeed") ||
                        effectName.equalsIgnoreCase("potion.heal")) {

                    if(shouldThrow(potionEffect.get(0))) {
                        throwPot(itemStack, i);
                        return;
                    }
                }
            }
        }
    }

    public void throwPot(ItemStack itemStack, int potSlot) {
        int originalSlot = mc.thePlayer.inventory.currentItem;

        mc.thePlayer.inventory.currentItem = potSlot;
        PacketManager.sendPacketWithoutEvent(new C09PacketHeldItemChange(potSlot));
        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX,
                mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, 90.0f, mc.thePlayer.onGround));
        PacketManager.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(itemStack));
        PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX,
                mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround));
        PacketManager.sendPacketWithoutEvent(new C09PacketHeldItemChange(originalSlot));
        mc.thePlayer.inventory.currentItem = originalSlot;
    }

    public boolean shouldThrow(PotionEffect potionEffect) {
        if(!potionEffect.isSplashPotion) return false;
        if(!mc.thePlayer.onGround) return false;
        if (mc.currentScreen instanceof GuiContainer) return false;

        Collection<PotionEffect> playerEffects = mc.thePlayer.getActivePotionEffects();
        System.out.println("Player Effects: " + playerEffects);
        System.out.println(mc.thePlayer.getHealth());

        if(killaura.toggled && !(Killaura.rotationMode.currentValue.equals("Off") || Killaura.rotationMode.currentValue.equals("Fake"))) {
            return false;
        }
        if(scaffold.toggled && !Scaffold.scaffoldMode.equals("Vanilla")) {
            return false;
        }

        if(!playerEffects.isEmpty()) {
            for(PotionEffect potEffect : playerEffects) {
                if(potionEffect.getPotionID() == potEffect.getPotionID()) {
                    return false;
                }
            }
        }

        //Check for InstantHealth Pots
        if(potionEffect.getPotionID() == 6 && mc.thePlayer.getHealth() > 12.0f) {
            return false;
        }

        return true;
    }

}
