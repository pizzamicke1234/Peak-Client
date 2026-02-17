package peak.modules.misc;

import org.lwjgl.input.Keyboard;
import peak.modules.Module;
import peak.events.TickEvent;
import peak.ui.notifications.Notification;
import peak.ui.notifications.NotificationManager;

public class TestModule extends Module {

    public TestModule() {
        super("TestModule", Keyboard.KEY_J, Category.MISC, true);
    }

    Notification notification;

    @Override
    public void onEnable() {
        notification = new Notification("Titeeeeeeeeeeele", "Test Message", Notification.NotificationType.INFO, 3000);
        NotificationManager.addNotification(notification);
    }

    @Override
    public void onDisable() {
        NotificationManager.removeNotification(notification);
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if(tickType == TickEvent.TickType.POST) return;

    }
}
