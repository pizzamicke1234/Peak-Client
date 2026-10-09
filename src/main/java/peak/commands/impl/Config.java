package peak.commands.impl;

import peak.commands.Command;
import peak.managers.file.ConfigSave;
import peak.managers.file.FileManager;
import peak.ui.notifications.Notification;
import peak.ui.notifications.NotificationManager;

import java.util.ArrayList;

public class Config extends Command {

    public Config() {
        super("config");
    }

    @Override
    public void onToggle(String[] args) {

        if(args.length == 3) {
            String operator = args[1];
            String name = args[2];

            if(operator.equalsIgnoreCase("save")) {
                saveConfig(name);
            }else if(operator.equalsIgnoreCase("load")){
                loadConfig(name);
            }else if(operator.equalsIgnoreCase("delete")){
                deleteConfig(name);
            }else {
                NotificationManager.addChat("§c.config <save/load/delete> <name>");
            }
            return;
        }

        if(args.length == 2 && args[1].equalsIgnoreCase("list")) {
            listConfigs();
            return;
        }

        NotificationManager.addChat("§c.config <save/load/delete/list> <name>");

    }

    private void saveConfig(String name) {
        FileManager.createConfigSave(name);
        Notification n = new Notification("Config saved", name + " saved!",
                Notification.NotificationType.INFO, 5000);
        NotificationManager.addNotification(n);
    }

    private void loadConfig(String name) {
        FileManager.readConfigSave(name);
        Notification n = new Notification("Config loaded", name + " loaded!",
                Notification.NotificationType.INFO, 5000);
        NotificationManager.addNotification(n);
    }

    private void listConfigs() {
        ArrayList<String> configSaves = FileManager.listConfigSavesbyName();
        NotificationManager.addChat("------------------------");
        for(String s : configSaves) {
            NotificationManager.addChat(s);
        }
        NotificationManager.addChat("------------------------");
    }

    private void deleteConfig(String name) {
        FileManager.deleteConfigSave(name);
        Notification n = new Notification("Config deleted", name + " deleted!",
                Notification.NotificationType.INFO, 5000);
        NotificationManager.addNotification(n);
    }

}
