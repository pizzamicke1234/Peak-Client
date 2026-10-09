package peak.managers.file;

import peak.modules.Module;
import peak.modules.settings.Setting;

import java.util.ArrayList;

public class ModuleSave {

    public String name;
    public boolean toggled;
    public ArrayList<SettingsSave> settingsSaves = new ArrayList<>();

    public ModuleSave(Module module) {
        this.name = module.getName();
        this.toggled = module.toggled;

        for(Setting s : module.getSettings()) {
            this.settingsSaves.add(new SettingsSave(s));
        }
    }
}
