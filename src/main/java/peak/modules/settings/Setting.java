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

        if(boundSetting instanceof BoolSetting) {
            boolean status = ((BoolSetting) boundSetting).status;
            return Arrays.asList(neededValues).contains(Boolean.toString(status));
        }

        return Arrays.asList(neededValues).contains(boundSetting.currentValue);
    }

    public void updateStatus() {
        if(boundSetting == null || neededValues == null) {
            return;
        }

        this.display = shouldDisplay(boundSetting, neededValues);
    }

}
