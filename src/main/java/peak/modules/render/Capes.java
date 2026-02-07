package peak.modules.render;

import peak.modules.Module;
import peak.modules.settings.BoolSetting;
import peak.modules.settings.ModeSetting;

public class Capes extends Module {

    public ModeSetting capeMode = new ModeSetting("Cape", false, "Peak", "Peak", "Epstein", "Hoppo");
    public BoolSetting everyPlayer = new BoolSetting("Every Player", false, false);

    public Capes() {
        super("Capes", 0, Category.RENDER, true);
        addSetting(capeMode, everyPlayer);
    }

}
