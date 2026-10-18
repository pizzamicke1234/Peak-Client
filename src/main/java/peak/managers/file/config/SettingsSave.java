package peak.managers.file.config;

import peak.modules.settings.BoolSetting;
import peak.modules.settings.Setting;

public class SettingsSave {

    public String name, currentValue;
    public boolean onArraylist;

    public SettingsSave(Setting setting) {
        this.name = setting.name;
        this.onArraylist = setting.onArraylist;
        if(setting instanceof BoolSetting) {
            this.currentValue = (((BoolSetting) setting).status == true) ? "true" : "false";
        }else {
            this.currentValue = setting.currentValue;
        }
    }
}
