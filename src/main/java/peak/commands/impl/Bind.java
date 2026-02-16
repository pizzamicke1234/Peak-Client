package peak.commands.impl;

import org.lwjgl.input.Keyboard;
import peak.Client;
import peak.commands.Command;
import peak.managers.NotificationManager;
import peak.modules.Module;

import java.awt.event.KeyEvent;

public class Bind extends Command {

    public Bind() {
        super("bind");
    }

    @Override
    public void onToggle(String[] args) {

        if(args.length > 2) {

            Module selectedModule = Client.getModulebyName(args[1]);
            String keyString = args[2];
            int keybind = Keyboard.getKeyIndex(keyString.toUpperCase());
            System.out.println(keybind);

            if(selectedModule == null) {
                NotificationManager.addChat("§cInvalid Module!");
                return;
            }

            selectedModule.setKey(keybind);
            NotificationManager.addChat("Binded " + selectedModule.name + " to " + args[2].toUpperCase());

        }

    }
}
