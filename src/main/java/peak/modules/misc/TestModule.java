package peak.modules.misc;

import net.minecraft.entity.Entity;
import org.lwjgl.input.Keyboard;

import peak.events.RenderEvent;
import peak.managers.render.RenderManager;
import peak.modules.Module;

import java.awt.*;


public class TestModule extends Module {


    public TestModule() {
        super("TestModule", Keyboard.KEY_J, Category.MISC, true);
    }

    @Override
    public void onRender(RenderEvent renderEvent) {

        float partialTicks = renderEvent.getPartialTicks();

        Color idleColor = new Color(44, 112, 255, 95);
        Color damageColor = new Color(255, 72, 72, 95);

        for(Entity entity : mc.theWorld.loadedEntityList) {
            RenderManager.drawMark(entity, partialTicks, damageColor);
        }

    }
}