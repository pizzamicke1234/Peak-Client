package peak.modules.render;

import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;
import peak.events.RenderEvent;
import peak.managers.render.RenderManager;
import peak.modules.Module;

import java.awt.*;
import java.util.List;

public class ESP extends Module {

    public ESP() {
        super("ESP", Keyboard.KEY_NONE, Category.RENDER, true);
    }

    Color espColor = new Color(255, 50, 50, 180);

    @Override
    public void onRender(RenderEvent renderEvent) {

        List<EntityPlayer> loadedPlayers = mc.theWorld.playerEntities;

        for(EntityPlayer player : loadedPlayers) {
            if(player == mc.thePlayer) continue;
            RenderManager.drawEntityESP(player, renderEvent.getPartialTicks(), espColor);
        }

    }
}
