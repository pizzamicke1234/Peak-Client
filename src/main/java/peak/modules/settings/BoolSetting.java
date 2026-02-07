package peak.modules.settings;

public class BoolSetting extends Setting{

    public boolean status;

    public BoolSetting(String name, boolean onArrayList, boolean status) {
        this.name = name;
        this.status = status;
        this.onArraylist = onArrayList; //Will probably never be displayed
    }

    public boolean isTrue() {
        return status;
    }

    public void toggle() {
        this.status = !this.status;
    }

    public void enable() {
        this.status = true;
    }

    public void disable() {
        this.status = false;
    }

}
