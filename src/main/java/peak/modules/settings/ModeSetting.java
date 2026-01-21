package peak.modules.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting{

    public List<String> modes;

    public ModeSetting(String name, String defaultmode, boolean showonArraylist, String... modes) {
        this.name = name;
        this.modes = Arrays.asList(modes);
        this.current_value = defaultmode;
        this.showonArraylist = showonArraylist;

    }

    public void nextMode() {
        int index = modes.indexOf(current_value);

        int nextIndex = (index + 1) % modes.size();
        current_value = modes.get(nextIndex);
    }

    public void setMode(String mode) {
        if (modes.contains(mode)) {
            this.current_value = mode;
        }
    }

}
