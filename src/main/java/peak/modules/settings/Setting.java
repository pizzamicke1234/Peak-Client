package peak.modules.settings;

import java.util.Arrays;

public abstract class Setting {

    public String name;
    public boolean onArraylist;
    public String currentValue;
    public boolean display = true;

    public Setting boundSetting;
    public String[] neededValues;

    public String getValue() {
        return currentValue;
    }

    protected boolean shouldDisplay(Setting boundSetting, String[] neededValues) {
        return Arrays.asList(neededValues).contains(boundSetting.currentValue);
    }

    public void updateStatus() {
        if(boundSetting == null || neededValues == null) {
            return;
        }

        this.display = shouldDisplay(boundSetting, neededValues);
    }

}
