package peak.modules;

import net.minecraft.client.Minecraft;
import peak.modules.settings.Setting;

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

    public void on_Enable() {

    }

    public void on_Disable() {

    }

    public void on_Tick() {

    }

    public void toggle() {
        toggled = !toggled;
        if(toggled) {
            on_Enable();
        }else{
            on_Disable();
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
