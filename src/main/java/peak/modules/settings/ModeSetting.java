package peak.modules.settings;

import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting{

    public List<String> modes;

    public ModeSetting(String name, boolean onArraylist, String defaultmode, String... modes) {
        this.name = name;
        this.modes = Arrays.asList(modes);
        this.currentValue = defaultmode;
        this.onArraylist = onArraylist;

    }

    public void nextMode() {
        int index = modes.indexOf(currentValue);

        int nextIndex = (index + 1) % modes.size();
        currentValue = modes.get(nextIndex);
    }

    public void setMode(String mode) {
        if (modes.contains(mode)) {
            this.currentValue = mode;
        }
    }

}
