package peak.modules.combat;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import org.lwjgl.input.Keyboard;
import peak.modules.Module;
import peak.modules.settings.BoolSetting;
import peak.modules.settings.ModeSetting;
import peak.modules.settings.NumberSetting;
import peak.events.TickEvent;

import java.util.Random;

public class Killaura extends Module {

    public ModeSetting killauramode = new ModeSetting("Mode", true, "Vanilla", "Vanilla", "Vulcan");
    //public ModeSetting hitmode = new ModeSetting("")
    public ModeSetting autoblock = new ModeSetting("Autoblock", false, "Off", "Off", "Vanilla", "Fake");
    public NumberSetting mincps = new NumberSetting("MinCPS", false, 1, 20, 10, 1);
    public NumberSetting maxcps = new NumberSetting("MaxCPS", false, 1, 20, 10, 1);

    public NumberSetting reach = new NumberSetting("Reach", true, 1, 8, 3, 0.20);

    public BoolSetting keepSprint = new BoolSetting("KeepSprint", false, false);

    public Random random = new Random();

    private int lastTick = -1;
    public static boolean fakeblocking = false;

    public Killaura() {
        super("Killaura", Keyboard.KEY_B, Category.COMBAT, true);
        addSetting(killauramode,reach,  maxcps, mincps, autoblock, keepSprint);
    }


    @Override
    public void onDisable() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
        fakeblocking = false;
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {

        switch (killauramode.current_value) {
            case "Vanilla":
                vaillaKillaura(tickType);
                break;

            case "Vulcan":
                vulcanKillaura(tickType);
                break;
        }
    }

    public boolean canClick() {
        if (mc.thePlayer.ticksExisted == lastTick) {
            return false;
        }

        double currentCPS = mincps.cValue + (maxcps.cValue - mincps.cValue) * random.nextDouble();
        if (random.nextDouble() < (currentCPS / 20.0)) {
            lastTick = mc.thePlayer.ticksExisted;
            return true;
        }
        return false;
    }

    public void vaillaKillaura(TickEvent.TickType tickType) {

        if(tickType == TickEvent.TickType.POST) return;
        if(!canClick()) return;

        ItemStack usedItem = mc.thePlayer.getHeldItem();

        for(Entity e : mc.theWorld.loadedEntityList) {

            if(e == mc.thePlayer || e == null || e instanceof EntityArmorStand) {
                continue;
            }

            if(e instanceof EntityLivingBase) {
                float distance = mc.thePlayer.getDistanceToEntity(e);

                if(distance <= reach.cValue * 1.1) {

                    manageAutoblock(e);
                    mc.thePlayer.swingItem();
                    mc.playerController.attackEntity(mc.thePlayer, e);

                }
            }
        }
    }

    public void vulcanKillaura(TickEvent.TickType tickType) {

        if(tickType == TickEvent.TickType.POST) {
            return;
        }

        if(!canClick()) return;

        Entity selectedtarget = null;
        ItemStack usedItem = mc.thePlayer.getHeldItem();

        for(Entity e : mc.theWorld.loadedEntityList) {

            if(e == mc.thePlayer || e == null || e instanceof EntityArmorStand) {
                continue;
            }

            if(e instanceof EntityLivingBase) {
                float distance = mc.thePlayer.getDistanceToEntity(e);
                if(distance <= reach.cValue * 1.1) {

                    if(selectedtarget == null) {
                        selectedtarget = e;
                    }
                }
            }

        }

        if(selectedtarget == null){
            fakeblocking = false;
            return;
        }

        manageAutoblock(selectedtarget);

        mc.thePlayer.swingItem();
        mc.playerController.attackEntity(mc.thePlayer, selectedtarget);

    }

    public void manageAutoblock(Entity e) {

        if(mc.thePlayer.getHeldItem() == null) return;

        Item helditem = mc.thePlayer.getHeldItem().getItem();

        if(autoblock.current_value == "Off" || helditem == null){
            return;
        }

        if(helditem instanceof  ItemSword) {
            if(autoblock.current_value == "Fake") {
                fakeblocking = true;
            }else {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
            }
        }else {
            if(autoblock.current_value == "Fake") {
                fakeblocking = false;
            }else {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
            }
        }

    }

}
