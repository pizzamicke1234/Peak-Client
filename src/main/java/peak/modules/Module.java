package peak.modules;

import net.minecraft.client.Minecraft;

public class Module {

    public String name;
    public int key;
    public boolean toggled;
    public Category category;

    public Minecraft mc = Minecraft.getMinecraft();

    public Module(String name, int key, Category category) {

        this.name = name;
        this.key = key;
        this.category = category;

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

    public enum Category {
        COMBAT,
        MOVEMENT,
        PLAYER,
        RENDER,
        MISC;
    }

}
