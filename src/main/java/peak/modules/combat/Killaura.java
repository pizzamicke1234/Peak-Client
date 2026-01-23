package peak.modules.combat;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.world.gen.ChunkProviderSettings;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import peak.modules.Module;
import peak.modules.settings.BoolSetting;
import peak.modules.settings.ModeSetting;
import peak.modules.settings.NumberSetting;
import peak.tickevents.TickEvent;

import java.util.Random;

public class Killaura extends Module {

    public ModeSetting killauramode = new ModeSetting("Mode", true, "Vanilla", "Vanilla", "Vulcan");
    //public ModeSetting hitmode = new ModeSetting("")
    public BoolSetting autoblock = new BoolSetting("Autoblock", false, false);
    public NumberSetting mincps = new NumberSetting("MinCPS", true, 1, 20, 10, 1);
    public NumberSetting maxcps = new NumberSetting("MaxCPS", true, 1, 20, 10, 1);

    public Random random = new Random();

    public double legitreach = 3.4;

    public Killaura() {
        super("Killaura", Keyboard.KEY_B, Category.COMBAT, true);
        addSetting(killauramode, maxcps, mincps, autoblock);
    }


    @Override
    public void on_Disable() {
        if(autoblock.isTrue()) KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
    }

    @Override
    public void on_Tick(TickEvent.TickType tickType) {

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
        int randomcps = random.nextInt((int)maxcps.cValue - (int)mincps.cValue + 1) + (int)mincps.cValue;
        if(mc.thePlayer.ticksExisted % (20 / randomcps) != 0) return false;
        return true;
    }

    public void vaillaKillaura(TickEvent.TickType tickType) {

        if(tickType == TickEvent.TickType.POST) return;

        if(!canClick()) return;

        for(Entity e : mc.theWorld.loadedEntityList) {
            ItemStack usedItem = mc.thePlayer.getHeldItem();

            if(e == mc.thePlayer || e == null) {
                continue;
            }

            if(e instanceof EntityLivingBase) {
                float distance = mc.thePlayer.getDistanceToEntity(e);

                if(distance <= 7) {

                    if(autoblock.isTrue() && usedItem != null) {
                        if(usedItem.getItem() instanceof ItemSword) KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                        else KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                    }else {
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                    }
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

        for(Entity e : mc.theWorld.loadedEntityList) {
            ItemStack usedItem = mc.thePlayer.getHeldItem();

            if(e == mc.thePlayer || e == null) {
                continue;
            }

            if(e instanceof EntityLivingBase) {
                float distance = mc.thePlayer.getDistanceToEntity(e);

                if(distance <= legitreach) {

                    if(selectedtarget == null) {
                        selectedtarget = e;
                    }

                    if(autoblock.isTrue() && usedItem != null) {
                        if(usedItem.getItem() instanceof ItemSword) KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                        else KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                    }else {
                        KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                    }

                    System.out.println("Distance: " + distance);
                    //mc.thePlayer.swingItem();
                    mc.playerController.attackEntity(mc.thePlayer, selectedtarget);
                }
            }
        }

    }

}
