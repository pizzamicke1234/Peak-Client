package peak.modules.misc;

import org.lwjgl.input.Keyboard;
import peak.managers.render.HitBox;
import peak.managers.render.RenderManager;
import peak.modules.Module;
import peak.events.TickEvent;
import peak.ui.notifications.Notification;
import peak.ui.notifications.NotificationManager;

public class TestModule extends Module {

    public TestModule() {
        super("TestModule", Keyboard.KEY_J, Category.MISC, true);
    }

    HitBox hitBox = new HitBox(3, 5, 3);

    @Override
    public void onEnable() {
        RenderManager.hitboxes.add(hitBox);
    }

    @Override
    public void onDisable() {
        RenderManager.hitboxes.remove(hitBox);
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if(tickType == TickEvent.TickType.POST) return;

    }
}
