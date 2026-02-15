package peak.modules.render;

import peak.modules.Module;
import peak.modules.settings.BoolSetting;
import peak.modules.settings.ModeSetting;

public class HUDMod extends Module{

    public ModeSetting logoMode = new ModeSetting("Logo", false, "Classic", "Classic", "New");
    public BoolSetting renderTest = new BoolSetting("Render Test", false, false);

    public HUDMod() {
        super("HUD", 0, Category.RENDER, true);
        this.addSetting(logoMode, renderTest);
    }

}
