package peak.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import peak.events.PacketEvent;
import peak.events.RenderEvent;
import peak.modules.settings.Setting;
import peak.events.TickEvent;
import peak.ui.notifications.Notification;
import peak.ui.notifications.NotificationManager;

import java.util.ArrayList;
import java.util.Arrays;

public class Module {

    public String name;
    public int key;
    public boolean toggled;
    public Category category;

    public boolean inClickGui;

    public ArrayList<Setting> settings = new ArrayList<Setting>();

    public Minecraft mc = Minecraft.getMinecraft();

    public Module(String name, int key, Category category, boolean inClickGui) {

        this.name = name;
        this.key = key;
        this.category = category;
        this.inClickGui = inClickGui;

    }

    public void onEnable() {

    }

    public void onDisable() {

    }

    public void onTick(TickEvent.TickType tickType) {

    }

    public void onPacket(PacketEvent packetEvent) {

    }

    public void onRender(RenderEvent renderEvent) {

    }

    public void toggle() {
        Notification notification;
        toggled = !toggled;
        if(toggled) {
            onEnable();
            notification = new Notification("Module Enabled", "Enabled " + name, Notification.NotificationType.MODULE, 1500);
        }else{
            onDisable();
            notification = new Notification("Module Disabled", "Disabled " + name, Notification.NotificationType.MODULE, 1500);
        }
        NotificationManager.addNotification(notification);
    }

    public String getName() {
        return name;
    }

    public void enable() {
        if(!toggled) {
            toggle();
        }
    }

    public void disable() {
        if(toggled) {
            toggle();
        }
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public void addSetting(Setting... settingstoadd) {
        settings.addAll(Arrays.asList(settingstoadd));
    }

    public ArrayList<Setting> getSettings() {
        return settings;
    }

    public enum Category {
        COMBAT,
        MOVEMENT,
        PLAYER,
        RENDER,
        MISC,
        FUN;
    }

}
