package peak.modules.settings;

public abstract class Setting {

    public String name;
    public boolean onArraylist;
    public String currentValue;

    public String getValue() {
        return currentValue;
    }

}
