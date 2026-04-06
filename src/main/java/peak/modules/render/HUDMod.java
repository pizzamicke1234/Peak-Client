package peak.modules.render;

import peak.modules.Module;
import peak.modules.settings.BoolSetting;
import peak.modules.settings.ModeSetting;

public class HUDMod extends Module{

    public ModeSetting logoMode = new ModeSetting("Logo", false, "Classic", "Classic", "New");
    public BoolSetting showTargetHud = new BoolSetting("Target Hud", false, false);

    public ModeSetting clickGuiStyle = new ModeSetting("ClickGui", false, "Default", "Default", "New");

    public HUDMod() {
        super("HUD", 0, Category.RENDER, true);
        this.addSetting(logoMode, showTargetHud, clickGuiStyle);
        this.toggled = true;
    }

}
