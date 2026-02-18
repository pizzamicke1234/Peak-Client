package peak.commands.impl;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;
import peak.commands.Command;
import peak.managers.PacketManager;
import peak.managers.render.HitBox;
import peak.managers.render.RenderManager;
import peak.ui.notifications.NotificationManager;

import java.util.ArrayList;

public class Teleport extends Command {

    public Teleport() {
        super("tp");
    }

    double packetX;
    int packetY;
    double packetZ;

    String serverIp;

    public static boolean allowC03Packets = true;

    ArrayList<BlockPos> tpPackets = new ArrayList<>();

    @Override
    public void onToggle(String[] args) {

        //serverIp = mc.getCurrentServerData().serverIP;
        serverIp = "play.deathzone.net";
        allowC03Packets = false;

        if(args.length == 2) {
            float oT = mc.timer.timerSpeed;
            mc.timer.timerSpeed = 0.1f;
            String playerName = args[1];
            EntityPlayer targetPlayer = getPlayerbyName(playerName);

            if(targetPlayer == null) {
                NotificationManager.addChat("§cUnkown Player");
                return;
            }

            BlockPos playerPos = new BlockPos(this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ);
            BlockPos targetPos = new BlockPos(targetPlayer.posX, targetPlayer.getEntityBoundingBox().minY, targetPlayer.posZ);
            int targetX = targetPos.getX();
            int targetY = targetPos.getY();
            int targetZ = targetPos.getZ();
            int distance = getDistanceToTarget(playerPos, targetPos);

            if(serverIp.equalsIgnoreCase("play.deathzone.net")) {
                distance *= 2;
            }

            packetX = playerPos.getX();
            packetY = playerPos.getY();
            packetZ = playerPos.getZ();


            //Packet collect
            tpPackets = getTpPackets(packetX, packetY, packetZ, targetX, targetY, targetZ, distance);

            //Teleport
            teleport(tpPackets);

            //RenderManager.hitboxes.clear();
            tpPackets.clear();
            NotificationManager.addChat("Distance: " + distance);

            mc.timer.timerSpeed = oT;
        }

        if(args.length == 4) {
            BlockPos playerPos = new BlockPos(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY, mc.thePlayer.posZ);
            int targetX = Integer.valueOf(args[1]);
            int targetY = Integer.valueOf(args[2]);
            int targetZ = Integer.valueOf(args[3]);
            BlockPos targetPos = new BlockPos(targetX, targetY, targetZ);
            int distance = getDistanceToTarget(playerPos, targetPos);

            if(serverIp.equalsIgnoreCase("play.deathzone.net")) {
                distance *= 2;
            }

            //Set the first packet Position
            packetX = playerPos.getX();
            packetY = playerPos.getY();
            packetZ = playerPos.getZ();


            //Packet collect
            tpPackets = getTpPackets(packetX, packetY, packetZ, targetX, targetY, targetZ, distance);

            //Teleport
            teleport(tpPackets);

            //RenderManager.hitboxes.clear();
            tpPackets.clear();
            NotificationManager.addChat("Distance: " + distance);
        }

        allowC03Packets = true;

    }

    public ArrayList<BlockPos> getTpPackets(double packetX, int packetY, double packetZ, int targetX, int targetY, int targetZ, int distance) {
        ArrayList<BlockPos> tpPackets = new ArrayList<>();

        for(int i = 0; i < distance; i++) {

            if(packetX == targetX && packetY == targetY && packetZ == targetZ) {
                break;
            }

            packetX = correctX(packetX, targetX);
            packetY = correctY(packetY, targetY);
            packetZ = correctZ(packetZ, targetZ);
            BlockPos tpPacket = new BlockPos(packetX, packetY, packetZ);
            tpPackets.add(tpPacket);
        }
        return tpPackets;
    }

    public void teleport(ArrayList<BlockPos> tpPackets) {
        for(BlockPos tpPacket : tpPackets) {

            C03PacketPlayer.C04PacketPlayerPosition packet = new C03PacketPlayer.C04PacketPlayerPosition(tpPacket.getX() + 0.5D, tpPacket.getY(), tpPacket.getZ() + 0.5D, false);
            PacketManager.sendPacketWithoutEvent(packet);

            //Draw Hitboxes
            HitBox hitBox = new HitBox(tpPacket.getX() + 0.5, tpPacket.getY(), tpPacket.getZ() + 0.5);
            RenderManager.hitboxes.add(hitBox);
        }
        int idx = tpPackets.size() - 1;
        mc.thePlayer.setPosition(tpPackets.get(idx).getX() + 0.5D, tpPackets.get(idx).getY(), tpPackets.get(idx).getZ() + 0.5D);
    }

    public EntityPlayer getPlayerbyName(String playerName) {
        for(EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
            if(entityPlayer.getName().equalsIgnoreCase(playerName)) {
                return entityPlayer;
            }
        }
        return null;
    }

    public int getDistanceToTarget(BlockPos playerPos, BlockPos targetPos) {
        int dX = Math.abs(targetPos.getX() - playerPos.getX());
        int dY = Math.abs(targetPos.getY() - playerPos.getY());
        int dZ = Math.abs(targetPos.getZ() - playerPos.getZ());
        return Math.max(dX, Math.max(dY, dZ));
    }

    public double correctX(double playerX, int targetX) {
        double difference = Math.abs(targetX - playerX);
        double step = (difference % 2 == 0) ? 2 : 1;

        if(serverIp.equalsIgnoreCase("play.deathzone.net")) {
            step = 0.5f;
        }

        if(difference == 0) return playerX;

        double newX = (targetX > playerX) ? playerX + step : playerX - step;
        return newX;
    }

    public int correctY(int playerY, int targetY) {
        int difference = Math.abs(targetY - playerY);
        int step = (difference % 3 == 0) ? 3 : 1;

        /*if(serverIp.equalsIgnoreCase("play.deathzone.net")) {
            step = 1;
        }*/

        if(difference == 0) return playerY;

        int newY = (targetY > playerY) ? playerY + step : playerY - step;
        return newY;
    }

    public double correctZ(double playerZ, int targetZ) {
        double difference = Math.abs(targetZ - playerZ);
        double step = (difference % 2 == 0) ? 2 : 1;

        if(serverIp.equalsIgnoreCase("play.deathzone.net")) {
            step = 0.5;
        }

        if(difference == 0) return playerZ;

        double newZ = (targetZ > playerZ) ? playerZ + step : playerZ - step;
        return newZ;
    }

}
