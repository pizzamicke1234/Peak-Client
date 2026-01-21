package peak.modules.settings;

public abstract class Setting {

    public String name;
    public boolean showonArraylist;
    public String current_value;

    public String getValue() {
        return current_value;
    }

}
