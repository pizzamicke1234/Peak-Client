package peak.commands;

import net.minecraft.client.Minecraft;

public class Command {

    String name;
    public Minecraft mc = Minecraft.getMinecraft();

    public Command(String name) {
        this.name = name;
    }

    public void onToggle(String[] args) {

    }

    public String getName() {
        return name;
    }
}
