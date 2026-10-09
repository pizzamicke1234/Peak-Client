package peak.managers.file;

import java.util.ArrayList;

public class ConfigSave {

    String configName;
    ArrayList<ModuleSave> moduleSaves;

    public ConfigSave(String configName, ArrayList<ModuleSave> moduleSaves) {
        this.configName = configName;
        this.moduleSaves = moduleSaves;
    }
}
