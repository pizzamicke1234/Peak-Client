package peak.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import peak.events.PacketEvent;
import peak.modules.settings.Setting;
import peak.events.TickEvent;

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

    public void toggle() {
        toggled = !toggled;
        if(toggled) {
            onEnable();
        }else{
            onDisable();
        }
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
        MISC;
    }

}
