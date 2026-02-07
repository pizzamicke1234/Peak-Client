package peak.modules.player;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import peak.events.PacketEvent;
import peak.events.TickEvent;
import peak.managers.NotificationManager;
import peak.managers.PacketManager;
import peak.managers.font.FontUtil;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;

public class Scaffold extends Module {

    public static ModeSetting scaffoldMode = new ModeSetting("Mode", false, "Normal", "Normal", "Vulcan");

    public Scaffold() {
        super("Scaffold", Keyboard.KEY_G, Category.PLAYER, true);
        addSetting(scaffoldMode);
    }

    public static float serverYaw;
    public static float serverPitch;

    int placedBlocks = 0;

    @Override
    public void onEnable() {
        placedBlocks = 0;
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1f;
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if(tickType == TickEvent.TickType.POST) return;

        switch (scaffoldMode.current_value) {
            case "Normal":
                normalScaffold();
                break;

            case "Vulcan":
                vulcanScaffold();
                break;
        }
    }

    @Override
    public void onPacket(PacketEvent packetEvent) {

        if(scaffoldMode.current_value.equals("Vulcan")) {

            if(packetEvent.getPacket() instanceof C03PacketPlayer) {
                serverYaw = mc.thePlayer.rotationYaw - 180;
                serverPitch = 87;
            }

            if(!(packetEvent.getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook)) return;

            BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ);
            BlockPos underPos = playerPos.down();

            ItemStack currentItem = mc.thePlayer.getHeldItem();

            if(getBlockCount() != 0 && isValidBlock(underPos) && currentItem != null && currentItem.getItem() instanceof ItemBlock) {
                serverYaw = mc.thePlayer.rotationYaw - 180;
                serverPitch = 87;
                packetEvent.cancelPacket();
                PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX,
                        mc.thePlayer.posY, mc.thePlayer.posZ, serverYaw, serverPitch, mc.thePlayer.onGround));
            }

        }

    }

    private void normalScaffold() {
        if(getBlockCount() == 0) return;

        serverYaw = mc.thePlayer.rotationYaw;
        serverPitch = mc.thePlayer.rotationPitch;

        BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ);
        BlockPos underPos = playerPos.down();

        ItemStack currentItem = mc.thePlayer.getHeldItem();

        if(isValidBlock(underPos) && currentItem != null && currentItem.getItem() instanceof ItemBlock) {
            PacketManager.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(underPos, blockDirection(), currentItem, 0.5f, 0.1f, 1));
            placedBlocks++;
        }
    }

    private void vulcanScaffold() {
        if(getBlockCount() == 0) return;

        serverYaw = mc.thePlayer.rotationYaw - 180;
        serverPitch = 87;

        BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ);
        BlockPos underPos = playerPos.down();

        ItemStack currentItem = mc.thePlayer.getHeldItem();

        if(isValidBlock(underPos) && currentItem != null && currentItem.getItem() instanceof ItemBlock) {

            /*if(mc.timer.timerSpeed > 1) {
                if(placedBlocks % 10 == 0) {
                    mc.timer.timerSpeed = 0.8f;
                }
            }else if(mc.timer.timerSpeed <= 1) {
                if(placedBlocks % 2 == 0) {
                    mc.timer.timerSpeed = 1.5f;
                }
            }*/

            if(placedBlocks % 4 == 0) {
                PacketManager.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.START_SNEAKING));
                PacketManager.sendPacketWithoutEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SNEAKING));
            }

            mc.thePlayer.setSprinting(false);
            PacketManager.sendPacketWithoutEvent(new C08PacketPlayerBlockPlacement(underPos, blockDirection(), currentItem, 0.5f, 0.1f, 1));
            placedBlocks++;
        }
    }

    private boolean isValidBlock(BlockPos pos) {
        Block block = mc.theWorld.getBlockState(pos).getBlock();
        return !(block instanceof BlockAir) && !block.getMaterial().isLiquid();
    }

    private int blockDirection() {
        if(mc.gameSettings.keyBindJump.isKeyDown()) return 1;

        float yaw = MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw);
        if((135.0f <= yaw && yaw <= 180.0f) || (-180.0f <= yaw && yaw < -135.0f)) return 2;
        if(-45.0f <= yaw && yaw < 45.0f) return 3;
        if(45.0f <= yaw && yaw < 135.0f) return 4;
        if(-135.0f <= yaw && yaw < -45.0f) return 5;
        return 1;
    }

    public static void renderBlockCount() {
        int blockCount = getBlockCount();
        FontUtil.normal.drawCenteredString(String.valueOf(blockCount), GuiScreen.width / 2, 425, -1);
    }

    public static int getBlockCount() {
        int blockCount = 0;
        for(ItemStack itemStack : Minecraft.getMinecraft().thePlayer.inventory.mainInventory) {
            if(itemStack != null && itemStack.getItem() instanceof ItemBlock) {
                blockCount += itemStack.stackSize;
            }
        }
        return blockCount;
    }

}
