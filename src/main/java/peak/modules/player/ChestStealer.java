package peak.modules.player;

import net.minecraft.client.gui.inventory.GuiChest;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import org.lwjgl.input.Keyboard;
import peak.modules.Module;
import peak.events.TickEvent;
import peak.modules.settings.NumberSetting;

import java.util.Random;

public class ChestStealer extends Module {

    public NumberSetting minDelay = new NumberSetting("MinDelay", false, 1, 20, 5, 1);
    public NumberSetting maxDelay = new NumberSetting("MaxDelay", false, 1, 20, 5, 1);

    public Random random = new Random();
    private int lastTick = -1;

    public ChestStealer() {
        super("ChestStealer", Keyboard.KEY_F, Category.PLAYER, true);
        addSetting(maxDelay, minDelay);
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {


        boolean shouldClose = true;

        if(mc.currentScreen instanceof GuiChest) {
            GuiChest guiChest = (GuiChest) mc.currentScreen;
            Container container = guiChest.inventorySlots;

            String containerName = guiChest.lowerChestInventory.getDisplayName().getUnformattedText();
            //Check if the opened container is a chest otherwise return
            if(!(containerName.equalsIgnoreCase("Large Chest") || containerName.equalsIgnoreCase("Chest"))) return;


            //Search every slot in the container
            for(Slot slot : container.inventorySlots) {
                int slotId = slot.slotNumber;
                ItemStack item = slot.getStack();

                //Checks if the item is in the container
                if(slotId <= 26 && item != null && shouldSteal(item)) {
                    shouldClose = false;
                    //Check for empty slots in player inventory
                    for(Slot playerSlot : mc.thePlayer.inventoryContainer.inventorySlots) {

                        boolean empty = !playerSlot.getHasStack();
                        if (empty && canSteal()) {
                            mc.playerController.windowClick(container.windowId, slotId, 0, 1, mc.thePlayer);
                            return;
                        }

                    }
                }

            }

            if(shouldClose){
                mc.thePlayer.closeScreen();
                shouldClose = true;
            }

        }

    }

    public boolean canSteal() {
        if (mc.thePlayer.ticksExisted == lastTick) {
            return false;
        }

        int randomDelay = random.nextInt((int) (maxDelay.cValue - minDelay.cValue) + 1) + (int) minDelay.cValue;
        if (mc.thePlayer.ticksExisted % randomDelay == 0) {
            lastTick = mc.thePlayer.ticksExisted;
            return true;
        }
        return false;
    }


    public boolean shouldSteal(ItemStack itemStack) {
        Item item = itemStack.getItem();

        if (item instanceof ItemSword)
            return true;

        if (item instanceof ItemTool)
            return true;

        if (item instanceof ItemFood)
            return true;

        if (item instanceof ItemBlock)
            return true;

        if (item instanceof ItemEnderPearl)
            return true;

        if (item instanceof ItemArmor)
            return true;

        if (item instanceof ItemSnowball)
            return true;

        if (item instanceof ItemEgg)
            return true;

        if (item instanceof ItemMonsterPlacer)
            return true;

        if (item instanceof ItemBow)
            return true;

        if (item instanceof ItemPotion)
            return true;

        return false;
    }


}
