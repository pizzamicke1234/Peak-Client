package peak.modules.render;

import peak.modules.Module;
import peak.modules.settings.BoolSetting;

public class HUDMod extends Module{

    public BoolSetting renderTest = new BoolSetting("Render Test", false, false);

    public HUDMod() {
        super("HUD", 0, Category.RENDER, true);
        this.addSetting(renderTest);
    }

}
