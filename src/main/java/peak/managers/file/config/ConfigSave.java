package peak.managers.file.config;

import java.util.ArrayList;

public class ConfigSave {

    String configName;
    public ArrayList<ModuleSave> moduleSaves;

    public ConfigSave(String configName, ArrayList<ModuleSave> moduleSaves) {
        this.configName = configName;
        this.moduleSaves = moduleSaves;
    }
}
