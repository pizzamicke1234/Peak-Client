package peak.commands.impl;

import peak.commands.Command;
import peak.ui.notifications.NotificationManager;

public class Test extends Command {

    public Test() {
        super("test");
    }

    @Override
    public void onToggle(String[] args) {
        NotificationManager.addChat("Test command executed");

        if(args.length > 1) {
            if(args[1].equals("1")) {
                NotificationManager.addChat("2");
            }
        }

    }
}
