package peak.modules.settings;

public abstract class Setting {

    public String name;
    public boolean onArraylist;
    public String currentValue;
    public boolean display = true;

    public Setting boundSetting;
    public String neededValue;

    public String getValue() {
        return currentValue;
    }

    protected boolean shouldDisplay(Setting boundSetting, String neededValue) {
        return boundSetting.currentValue.equals(neededValue);
    }

    public void updateStatus() {
        if(boundSetting == null || neededValue == null) {
            return;
        }

        this.display = shouldDisplay(boundSetting, neededValue);
    }

}
