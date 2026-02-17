package peak.commands.impl;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import peak.commands.Command;
import peak.managers.PacketManager;
import peak.managers.render.HitBox;
import peak.managers.render.RenderManager;
import peak.ui.notifications.Notification;
import peak.ui.notifications.NotificationManager;

public class Teleport extends Command {

    public Teleport() {
        super("tp");
    }

    int packetX;
    int packetY;
    int packetZ;

    @Override
    public void onToggle(String[] args) {

        if(args.length > 1) {
            String playerName = args[1];
            EntityPlayer targetPlayer = getPlayerbyName(playerName);

            if(targetPlayer == null) {
                NotificationManager.addChat("§cUnkown Player");
                return;
            }

            BlockPos playerPos = new BlockPos(this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ);
            BlockPos targetPos = new BlockPos(targetPlayer.posX, targetPlayer.getEntityBoundingBox().minY, targetPlayer.posZ);

            packetX = playerPos.getX();
            packetY = playerPos.getY();
            packetZ = playerPos.getZ();

            correctX(playerPos, targetPos);
            correctY(playerPos, targetPos);
            correctZ(playerPos, targetPos);
            RenderManager.hitboxes.add(new HitBox(packetX + 0.5, packetY, packetZ + 0.5));
            PacketManager.sendPacketWithoutEvent(new C03PacketPlayer.C04PacketPlayerPosition(packetX + 0.5, packetY, packetZ + 0.5, false));
            mc.thePlayer.setPosition(packetX + 0.5, packetY, packetZ + 0.5);

            RenderManager.hitboxes.clear();
            NotificationManager.addChat("Done");

        }

    }

    public EntityPlayer getPlayerbyName(String playerName) {
        for(EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
            if(entityPlayer.getName().equalsIgnoreCase(playerName)) {
                return entityPlayer;
            }
        }
        return null;
    }

    public void correctX(BlockPos playerPos, BlockPos targetPos) {
        int targetX = targetPos.getX();
        int playerX = playerPos.getX();
        int difference = Math.abs(packetX - targetX);
        int step;

        step = (difference % 2 == 0) ? 2 : 1;
        packetX = (targetX > playerX) ? packetX + step : packetX - step;

    }

    public void correctY(BlockPos playerPos, BlockPos targetPos) {
        int targetY = targetPos.getY();
        int playerY = playerPos.getY();
        int difference = Math.abs(packetY - targetY);
        int step;

        step = (difference % 2 == 0) ? 2 : 1;
        packetY = (targetY > playerY) ? packetY + step : packetY - step;

    }

    public void correctZ(BlockPos playerPos, BlockPos targetPos) {
        int targetZ = targetPos.getZ();
        int playerZ = playerPos.getZ();
        int difference = Math.abs(packetZ- targetZ);
        int step;

        step = (difference % 2 == 0) ? 2 : 1;
        packetZ = (targetZ > playerZ) ? packetZ + step : packetZ - step;

    }

}
