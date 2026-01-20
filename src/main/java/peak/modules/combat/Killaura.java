package peak.modules.combat;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import peak.modules.Module;

public class Killaura extends Module {

    public Killaura() {
        super("Killaura", Keyboard.KEY_B, Category.COMBAT, true);
    }

    boolean autoblock = true;

    @Override
    public void on_Disable() {
        if(autoblock) KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
    }

    @Override
    public void on_Tick() {
        for(Entity e : mc.theWorld.loadedEntityList) {
            String entityType = EntityList.getEntityString(e);

            if(e == mc.thePlayer) {
                continue;
            }

            if(e instanceof EntityLivingBase) {
                float distance = mc.thePlayer.getDistanceToEntity(e);

                if(distance <= 4) {
                    if(autoblock) KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                    mc.playerController.attackEntity(mc.thePlayer, e);
                    mc.thePlayer.swingItem();
                }
            }
        }
    }

}
