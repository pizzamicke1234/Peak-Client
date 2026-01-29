package peak.modules.player;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.item.*;
import org.lwjgl.input.Keyboard;
import peak.modules.Module;
import peak.modules.settings.BoolSetting;
import peak.events.TickEvent;

import java.util.ArrayList;

public class InvManager extends Module {

    BoolSetting silent = new BoolSetting("Silent", false, false);

    public InvManager() {
        super("InvManager", Keyboard.KEY_K, Category.PLAYER, true);
        addSetting(silent);
    }

    @Override
    public void onEnable(){
        //sortWeapons();
        //sortArmor();
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if(tickType == TickEvent.TickType.POST) return;

        if(silent.isTrue() || mc.currentScreen instanceof GuiInventory) {
            sortWeapons();
            sortArmor();
        }

    }

    public void sortWeapons() {
        ItemStack bestStack = null;
        float maxDamage = -1;
        int bestSlot = -1;

        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
            if (itemStack != null && itemStack.getItem() instanceof ItemSword) {
                float currentDamage = getDamage(itemStack);
                if (currentDamage > maxDamage) {
                    maxDamage = currentDamage;
                    bestStack = itemStack;
                    bestSlot = i;
                }
            }
        }

        if (bestStack == null) return;

        int targetHotbarSlot = 0; //Sword Slot

        if (bestSlot != targetHotbarSlot) {
            int windowSlot = (bestSlot < 9) ? (bestSlot + 36) : bestSlot;

            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, windowSlot, targetHotbarSlot, 2, mc.thePlayer);
            return;
        }

        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];

            if (itemStack != null && itemStack.getItem() instanceof ItemSword && i != bestSlot) {
                int worseSlot = (i < 9) ? (i + 36) : i;

                mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, worseSlot, 1, 4, mc.thePlayer);
                break;
            }
        }
    }

    public void sortArmor() {

        sortHelmets();
        sortChestplates();
        sortLeggings();
        sortBoots();

    }

    public void sortHelmets() {
        ItemArmor bestHelmet = (ItemArmor) bestHelmet();
        if (bestHelmet == null) return;

        int bestHelmetSlot = getSlotOfItem(bestHelmet);
        ItemStack currentArmorStack = mc.thePlayer.inventoryContainer.getSlot(5).getStack();

        if (bestHelmetSlot != -1 && (currentArmorStack == null || currentArmorStack.getItem() != bestHelmet)) {
            int bestWindowSlot = (bestHelmetSlot < 9) ? (bestHelmetSlot + 36) : bestHelmetSlot;

            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestWindowSlot, 0, 0, mc.thePlayer);
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 5, 0, 0, mc.thePlayer);
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestWindowSlot, 0, 0, mc.thePlayer);
            return;
        }

        for (int i = 9; i < 45; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (stack != null && stack.getItem() instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor) stack.getItem();

                if (armor.armorType == 0) {
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 1, 4, mc.thePlayer);
                }
            }
        }

    }

    public void sortChestplates() {
        ItemArmor bestChestplate = (ItemArmor) bestChestplate();
        if (bestChestplate == null) return;

        int bestChestplateSlot = getSlotOfItem(bestChestplate);
        ItemStack currentArmorStack = mc.thePlayer.inventoryContainer.getSlot(6).getStack();

        if (bestChestplateSlot != -1 && (currentArmorStack == null || currentArmorStack.getItem() != bestChestplate)) {
            int bestWindowSlot = (bestChestplateSlot < 9) ? (bestChestplateSlot + 36) : bestChestplateSlot;

            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestWindowSlot, 0, 0, mc.thePlayer);
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 6, 0, 0, mc.thePlayer);
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestWindowSlot, 0, 0, mc.thePlayer);
            return;
        }

        for (int i = 9; i < 45; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (stack != null && stack.getItem() instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor) stack.getItem();

                if (armor.armorType == 1) {
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 1, 4, mc.thePlayer);
                }
            }
        }

    }

    public void sortLeggings() {
        ItemArmor bestLeggings = (ItemArmor) bestLeggings();
        if (bestLeggings == null) return;

        int bestLeggingsSlot = getSlotOfItem(bestLeggings);
        ItemStack currentArmorStack = mc.thePlayer.inventoryContainer.getSlot(7).getStack();

        if (bestLeggingsSlot != -1 && (currentArmorStack == null || currentArmorStack.getItem() != bestLeggings)) {
            int bestWindowSlot = (bestLeggingsSlot < 9) ? (bestLeggingsSlot + 36) : bestLeggingsSlot;

            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestWindowSlot, 0, 0, mc.thePlayer);
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 7, 0, 0, mc.thePlayer);
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestWindowSlot, 0, 0, mc.thePlayer);
            return;
        }

        for (int i = 9; i < 45; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (stack != null && stack.getItem() instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor) stack.getItem();

                if (armor.armorType == 2) {
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 1, 4, mc.thePlayer);
                }
            }
        }

    }

    public void sortBoots() {
        ItemArmor bestBoots = (ItemArmor) bestBoots();
        if (bestBoots == null) return;

        int bestBootsSlot = getSlotOfItem(bestBoots);
        ItemStack currentArmorStack = mc.thePlayer.inventoryContainer.getSlot(8).getStack();

        if (bestBootsSlot != -1 && (currentArmorStack == null || currentArmorStack.getItem() != bestBoots)) {
            int bestWindowSlot = (bestBootsSlot < 9) ? (bestBootsSlot + 36) : bestBootsSlot;

            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestWindowSlot, 0, 0, mc.thePlayer);
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, 8, 0, 0, mc.thePlayer);
            mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, bestWindowSlot, 0, 0, mc.thePlayer);
            return;
        }

        for (int i = 9; i < 45; i++) {
            ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(i).getStack();

            if (stack != null && stack.getItem() instanceof ItemArmor) {
                ItemArmor armor = (ItemArmor) stack.getItem();

                if (armor.armorType == 3) {
                    mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, i, 1, 4, mc.thePlayer);
                }
            }
        }

    }

    //TODO: Add Enchantments to Armor filter
    public ItemArmor bestHelmet() {
        ArrayList<ItemArmor> allArmor = getArmorinInv();
        ItemArmor bestHelmet = null;

        for(ItemArmor itemArmor : allArmor) {
            if(itemArmor.armorType == 0) {

                if(bestHelmet == null || itemArmor.damageReduceAmount > bestHelmet.damageReduceAmount) {
                    bestHelmet = itemArmor;
                }

            }
        }
        return bestHelmet;
    }

    public ItemArmor bestChestplate() {
        ArrayList<ItemArmor> allArmor = getArmorinInv();
        ItemArmor bestChestplate = null;

        for(ItemArmor itemArmor : allArmor) {
            if(itemArmor.armorType == 1) {

                if(bestChestplate == null || itemArmor.damageReduceAmount > bestChestplate.damageReduceAmount) {
                    bestChestplate = itemArmor;
                }

            }
        }
        return bestChestplate;
    }

    public ItemArmor bestLeggings() {
        ArrayList<ItemArmor> allArmor = getArmorinInv();
        ItemArmor bestLeggings = null;

        for(ItemArmor itemArmor : allArmor) {
            if(itemArmor.armorType == 2) {

                if(bestLeggings == null || itemArmor.damageReduceAmount > bestLeggings.damageReduceAmount) {
                    bestLeggings = itemArmor;
                }

            }
        }
        return bestLeggings;
    }

    public ItemArmor bestBoots() {
        ArrayList<ItemArmor> allArmor = getArmorinInv();
        ItemArmor bestBoots = null;

        for(ItemArmor itemArmor : allArmor) {
            if(itemArmor.armorType == 3) {

                if(bestBoots == null || itemArmor.damageReduceAmount > bestBoots.damageReduceAmount) {
                    bestBoots = itemArmor;
                }

            }
        }
        return bestBoots;
    }

    public ArrayList<ItemArmor> getArmorinInv() {
        ArrayList<ItemArmor> allArmor = new ArrayList<>();

        for(int i = 0; i < 36; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
            if(itemStack != null && itemStack.getItem() instanceof ItemArmor) {
                allArmor.add((ItemArmor) itemStack.getItem());
            }
        }

        for(int i = 0; i < 4; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.armorInventory[i];
            if(itemStack != null && itemStack.getItem() instanceof ItemArmor) {
                allArmor.add((ItemArmor) itemStack.getItem());
            }
        }
        return allArmor;
    }

    public float getDamage(ItemStack stack) {
        if (stack == null) return 1.0f;

        float damage = 0;

        if (stack.getItem() instanceof ItemSword) {
            damage = ((ItemSword) stack.getItem()).getDamageVsEntity() + 4.0f;
        } else if (stack.getItem() instanceof ItemTool) {
            damage = 1.0f;
        } else {
            damage = 1.0f;
        }

        damage += EnchantmentHelper.getModifierForCreature(stack, EnumCreatureAttribute.UNDEFINED);

        return damage;
    }

    public void moveItem(int fromSlot, int toSlot) {
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, fromSlot, 0, 0, mc.thePlayer);
        mc.playerController.windowClick(mc.thePlayer.inventoryContainer.windowId, toSlot, 0, 0, mc.thePlayer);
    }

    public int getSlotOfItem(Item item) {
        for (int i = 0; i < 36; i++) {
            ItemStack itemStack = mc.thePlayer.inventory.mainInventory[i];
            if (itemStack != null && itemStack.getItem() == item) {
                return i;
            }
        }
        return -1;
    }
}
