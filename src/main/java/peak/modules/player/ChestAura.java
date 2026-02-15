package peak.modules.player;

import net.minecraft.block.BlockChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import peak.events.TickEvent;
import peak.managers.NotificationManager;
import peak.modules.Module;

import java.util.ArrayList;

public class ChestAura extends Module {

    public ChestAura() {
        super("ChestAura", 0, Category.PLAYER, true);
    }

    ArrayList<BlockPos> openedChests = new ArrayList<>();

    @Override
    public void onEnable() {
        openedChests = new ArrayList<>();
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if(tickType == TickEvent.TickType.POST) return;

        //Reset on World Change
        if(mc.thePlayer.ticksExisted < 2) {
            openedChests = new ArrayList<>();
        }

        if (mc.currentScreen instanceof GuiContainer) return;

        for(TileEntity tileEntity : mc.theWorld.loadedTileEntityList) {
            if(tileEntity.getBlockType() instanceof BlockChest) {
                if(openedChests.contains(tileEntity.getPos())) return;
                int distance = getDistanceToTileEntity(tileEntity);

                if(distance <= 4) {
                    NotificationManager.addChat("BLY^^at");
                    if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem(), tileEntity.getPos(), EnumFacing.DOWN, new Vec3(tileEntity.getPos()))) {
                        mc.thePlayer.swingItem();
                        openedChests.add(tileEntity.getPos());
                        NotificationManager.addChat("BLYat");
                        return;
                    }
                }

            }
        }

    }

    private int getDistanceToTileEntity(TileEntity tileEntity) {
        BlockPos playerPos = new BlockPos(this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ);
        BlockPos tilePos = tileEntity.getPos();
        int distX = Math.abs(tilePos.getX() - playerPos.getX());
        int distY = Math.abs(tilePos.getY() - playerPos.getY());
        int distZ = Math.abs(tilePos.getZ() - playerPos.getZ());

        return Math.max(distX, Math.max(distY, distZ));
    }

}
