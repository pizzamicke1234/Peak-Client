package peak.modules.combat;

import com.viaversion.viaversion.protocols.v1_20to1_20_2.packet.ServerboundPacket1_20_2;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Keyboard;
import peak.events.PacketEvent;
import peak.managers.PacketManager;
import peak.managers.RotationManager;
import peak.modules.Module;
import peak.modules.settings.BoolSetting;
import peak.modules.settings.ModeSetting;
import peak.modules.settings.NumberSetting;
import peak.events.TickEvent;

import java.util.Random;

public class Killaura extends Module {

    public ModeSetting moveFixMode = new ModeSetting("MoveFix", false, "Off", "Off", "Vulcan");

    public ModeSetting targetMode = new ModeSetting("TargetMode", true, "Single", "Single", "Multi");
    public static ModeSetting rotationMode = new ModeSetting("Rotations", false, "Off", "Off", "Normal", "Fake");

    public ModeSetting autoblock = new ModeSetting("Autoblock", false, "Off", "Off", "Vanilla", "Vanilla1", "Fake");
    public NumberSetting mincps = new NumberSetting("MinCPS", false, 1, 20, 10, 1);
    public NumberSetting maxcps = new NumberSetting("MaxCPS", false, 1, 20, 10, 1);

    public NumberSetting reach = new NumberSetting("Reach", true, 1, 8, 3, 0.20);

    public BoolSetting keepSprint = new BoolSetting("KeepSprint", false, false);

    public Killaura() {
        super("Killaura", Keyboard.KEY_B, Category.COMBAT, true);
        addSetting(targetMode, reach, maxcps, mincps, rotationMode, autoblock, keepSprint, moveFixMode);
    }

    public static float serveryaw, serverpitch;

    public Random random = new Random();

    private int lastTick = -1;
    public static boolean fakeblocking = false;
    public static Entity selectedtarget = null;

    @Override
    public void onDisable() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
        fakeblocking = false;
        selectedtarget = null;
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if(tickType == TickEvent.TickType.POST) return;
        if(!canClick()) return;

        selectedtarget = null;

        for(Entity e : mc.theWorld.loadedEntityList) {

            if(e == mc.thePlayer || e == null || e instanceof EntityArmorStand || e.isDead) {
                continue;
            }

            if(e instanceof EntityLivingBase) {
                float distance = mc.thePlayer.getDistanceToEntity(e);

                if(distance <= reach.cValue) {

                    if(targetMode.currentValue.equals("Single")) {
                        if(selectedtarget == null || selectedtarget.isDead) {
                            selectedtarget = e;
                        }
                    } else {
                        selectedtarget = e;
                    }

                    manageRotations(selectedtarget, false);
                    manageAutoblock(selectedtarget);

                    mc.thePlayer.swingItem();
                    mc.playerController.attackEntity(mc.thePlayer, selectedtarget);
                    if(targetMode.currentValue.equals("Single")) {
                        return;
                    }

                }
            }
            fakeblocking = false;
        }

    }

    @Override
    public void onPacket(PacketEvent packetEvent) {
        Packet packet = packetEvent.getPacket();

        if(!rotationMode.currentValue.equals("Off") && !rotationMode.currentValue.equals("Fake")) {
            if(selectedtarget != null) {

                if(packet instanceof C03PacketPlayer) {

                }
                if(packet instanceof C03PacketPlayer.C06PacketPlayerPosLook || packet instanceof S08PacketPlayerPosLook) {
                    manageRotations(selectedtarget, true);
                    packetEvent.cancelPacket();
                }
            }
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

    public void manageAutoblock(Entity e) {

        if(mc.thePlayer.getHeldItem() == null) return;

        Item helditem = mc.thePlayer.getHeldItem().getItem();

        if(autoblock.currentValue == "Off" || helditem == null){
            return;
        }

        if(helditem instanceof  ItemSword) {

            switch (autoblock.currentValue) {

                case "Fake":
                    fakeblocking = true;
                    break;

                case "Vanilla":
                    PacketManager.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                    fakeblocking = true; //Display block animation client side
                    break;

                case "Vanilla1":
                    mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                    fakeblocking = true;
                    break;

            }

        }else {

            switch (autoblock.currentValue) {

                case "Fake":
                    fakeblocking = false;
                    break;

            }

        }

    }

    public void manageRotations(Entity e, boolean packetSend) {
        if(!rotationMode.currentValue.equals("Off") && !rotationMode.currentValue.equals("Fake") && selectedtarget != null) {
            serveryaw = RotationManager.getRotationsToEntity(e)[0];
            serverpitch = RotationManager.getRotationsToEntity(e)[1];
            RotationManager.lookSilent(new float[]{serveryaw, serverpitch}, 250, 250, packetSend);
        }
    }

}
