package peak.managers.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import peak.Client;
import peak.managers.file.altmanager.AccountListSave;
import peak.managers.file.altmanager.SessionSave;
import peak.managers.file.config.ConfigSave;
import peak.managers.file.config.ModuleSave;
import peak.managers.file.config.SettingsSave;
import peak.modules.Module;
import peak.modules.settings.BoolSetting;
import peak.modules.settings.NumberSetting;
import peak.modules.settings.Setting;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileManager {

    private final static File clientDirectory = new File(Minecraft.getMinecraft().mcDataDir, Client.name);
    private final static File configDirectory = new File(clientDirectory, "configs");
    private final static File altmanagerDirectory = new File(clientDirectory, "altmanager");

    private static File configSaveFile, altmanagerSaveFile;
    private final static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void createConfigSave(String configName) {
        createDirectory();

        configSaveFile = new File(configDirectory, configName + ".json");

        ArrayList<ModuleSave> moduleSaves = new ArrayList<>();
        for(Module m : Client.modules) {
            moduleSaves.add(new ModuleSave(m));
        }
        ConfigSave configSave = new ConfigSave(configName, moduleSaves);
        storeConfigSave(configSave);
    }

    public static void storeConfigSave(ConfigSave configSave) {

        try {

            FileWriter writer = new FileWriter(configSaveFile);
            gson.toJson(configSave, writer);
            writer.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readConfigSave(String name) {
        try {
            createDirectory();
            File file = new File(configDirectory, name + ".json");
            FileReader reader = new FileReader(file);
            ConfigSave configSave = gson.fromJson(reader, ConfigSave.class);
            loadConfigSave(configSave);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadConfigSave(ConfigSave configSave) {
        for(ModuleSave moduleSave : configSave.moduleSaves) {
            Module m = Client.getModulebyName(moduleSave.name);
            m.toggled = moduleSave.toggled;

            for(SettingsSave settingsSave : moduleSave.settingsSaves) {
                Setting s = m.getSettingbyName(settingsSave.name);
                s.onArraylist = settingsSave.onArraylist;

                if(s instanceof BoolSetting) {
                    ((BoolSetting) s).status = Boolean.parseBoolean(settingsSave.currentValue);
                } else if (s instanceof NumberSetting) {
                    ((NumberSetting) s).setcValue(Double.parseDouble(settingsSave.currentValue));
                } else {
                    s.currentValue = settingsSave.currentValue;
                }
            }
        }
    }

    public static void deleteConfigSave(String name) {
        File file = new File(configDirectory, name + ".json");
        file.delete();
    }

    public static ArrayList<String> listConfigSavesbyName() {
        ArrayList<String> configSaves = new ArrayList<>();
        createDirectory();

        for(File f : configDirectory.listFiles())  {
            configSaves.add(f.getName());
        }

        return configSaves;
    }

    public static void createAccountListSave(ArrayList<Session> sessions) {
        createDirectory();

        altmanagerSaveFile = new File(altmanagerDirectory, "accounts.json");

        ArrayList<SessionSave> sessionSaves = new ArrayList<>();
        for(Session s : sessions) {
            sessionSaves.add(new SessionSave(s));
        }
        AccountListSave accountListSave = new AccountListSave(sessionSaves);
        storeAccountListSave(accountListSave);
    }

    private static void storeAccountListSave(AccountListSave accountListSave) {
        try {

            FileWriter writer = new FileWriter(altmanagerSaveFile);
            gson.toJson(accountListSave, writer);
            writer.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static AccountListSave getAccountListSave() {
        try {
            createDirectory();
            File file = new File(altmanagerDirectory,"accounts.json");
            FileReader reader = new FileReader(file);
            AccountListSave accountListSave = gson.fromJson(reader, AccountListSave.class);
            return accountListSave;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void createDirectory() {
        if(!clientDirectory.exists()) {
            clientDirectory.mkdirs();
        }
        if(!configDirectory.exists()) {
            configDirectory.mkdirs();
        }

        if(!altmanagerDirectory.exists()) {
            altmanagerDirectory.mkdirs();
        }
    }

}
