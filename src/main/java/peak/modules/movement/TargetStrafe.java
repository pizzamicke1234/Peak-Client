package peak.modules.movement;

import net.minecraft.block.BlockAir;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.input.Keyboard;
import peak.events.TickEvent;
import peak.managers.MovementManager;
import peak.modules.Module;
import peak.modules.combat.Killaura;
import peak.modules.settings.NumberSetting;

public class TargetStrafe extends Module {

    public final NumberSetting range = new NumberSetting("Range", false, 1, 6, 3.8, 0.1);

    private Entity target;
    private int direction = 1;
    private int collisionTimer = 0;

    public TargetStrafe() {
        super("TargetStrafe", Keyboard.KEY_O, Category.MOVEMENT, true);
        this.settings.add(range);
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if (tickType == TickEvent.TickType.POST) return;

        target = Killaura.selectedtarget;

        if (target == null || !MovementManager.isMoving()) return;

        if (collisionTimer > 0) collisionTimer--;

        if (collisionTimer <= 0 && (mc.thePlayer.isCollidedHorizontally || checkVoid())) {
            direction *= -1;
            collisionTimer = 12;
        }

        float dist = mc.thePlayer.getDistanceToEntity(target);
        float yaw = getYawToEntity(target);

        double diff = (dist - range.cValue);
        double cos = (dist <= range.cValue + 0.1) ? 0 : diff / dist;
        double strafeAngle = Math.toDegrees(Math.acos(Math.min(1, Math.max(-1, cos))));

        float finalYaw = (float) (yaw + (strafeAngle * direction));

        double speed = MovementManager.getSpeed();
        double radians = Math.toRadians(finalYaw);

        mc.thePlayer.motionX = -Math.sin(radians) * speed;
        mc.thePlayer.motionZ = Math.cos(radians) * speed;

        mc.thePlayer.moveForward = 0;
        mc.thePlayer.moveStrafing = 0;
    }

    private float getYawToEntity(Entity entity) {
        double x = entity.posX - mc.thePlayer.posX;
        double z = entity.posZ - mc.thePlayer.posZ;
        return (float) (Math.toDegrees(Math.atan2(z, x)) - 90.0F);
    }

    private boolean checkVoid() {
        double x = mc.thePlayer.posX + (mc.thePlayer.motionX * 3);
        double z = mc.thePlayer.posZ + (mc.thePlayer.motionZ * 3);

        for (int i = -1; i > -5; i--) {
            if (!(mc.theWorld.getBlockState(new net.minecraft.util.BlockPos(x, mc.thePlayer.posY + i, z)).getBlock() instanceof BlockAir)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onDisable() {
        collisionTimer = 0;
    }
}