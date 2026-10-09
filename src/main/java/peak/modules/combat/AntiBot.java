package peak.modules.combat;

import net.minecraft.entity.Entity;
import peak.Client;
import peak.events.TickEvent;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;
import peak.ui.notifications.Notification;
import peak.ui.notifications.NotificationManager;

import java.util.ArrayList;

public class AntiBot extends Module {

    ModeSetting mode = new ModeSetting("Mode", true, "NoRules", new String[]{"NoRules"});

    public AntiBot() {
        super("AntiBot", 0, Category.COMBAT, true);
        this.addSetting(mode);
    }

    Killaura killaura = (Killaura) Client.getModulebyName("Killaura");

    @Override
    public void onTick(TickEvent.TickType tickType) {
        switch (mode.currentValue) {
            case "NoRules":
                for(Entity e : mc.theWorld.playerEntities) {
                    if(e.getName().equalsIgnoreCase("Watchdog") && !killaura.flaggedEntities.contains(e)) {
                        killaura.flaggedEntities.add(e);
                        Notification n = new Notification("AntiBot", "A Bot has appeared!",
                                Notification.NotificationType.WARNING, 5000);
                        NotificationManager.addNotification(n);
                    }
                }
        }
    }
}
