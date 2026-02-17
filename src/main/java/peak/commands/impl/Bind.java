package peak.commands.impl;

import org.lwjgl.input.Keyboard;
import peak.Client;
import peak.commands.Command;
import peak.ui.notifications.NotificationManager;
import peak.modules.Module;

public class Bind extends Command {

    public Bind() {
        super("bind");
    }

    @Override
    public void onToggle(String[] args) {

        if(args.length == 2 && args[1].equalsIgnoreCase("list")) {
            NotificationManager.addChat("---------------");
            for(Module module : Client.modules) {
                String keyName = Keyboard.getKeyName(module.getKey());
                if(keyName.equalsIgnoreCase("none")) continue;

                NotificationManager.addChat(module.name + ": " + keyName);
            }
            NotificationManager.addChat("---------------");
            return;
        }

        if(args.length > 2) {

            Module selectedModule = Client.getModulebyName(args[1]);
            String keyString = args[2];
            int keybind = Keyboard.getKeyIndex(keyString.toUpperCase());

            if(selectedModule == null) {
                NotificationManager.addChat("§cInvalid Module!");
                return;
            }

            selectedModule.setKey(keybind);
            NotificationManager.addChat("Binded " + selectedModule.name + " to " + args[2].toUpperCase());

        }

    }
}
