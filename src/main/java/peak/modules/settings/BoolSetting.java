package peak.modules.settings;

public class BoolSetting extends Setting{

    public boolean status;

    public BoolSetting(String name, boolean status, boolean showonArraylist) {
        this.name = name;
        this.status = status;
        this.showonArraylist = showonArraylist; //Will probably never be displayed
    }

    public boolean isTrue() {
        return status;
    }

}
