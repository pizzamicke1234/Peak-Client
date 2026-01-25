package peak.modules.combat;

import peak.modules.Module;
import peak.modules.settings.ModeSetting;

public class Velocity extends Module {

    public ModeSetting velocityMode = new ModeSetting("Mode", true, "Vanilla", "Vanilla", "Vulcan");

    public Velocity() {
        super("Velocity", 0, Category.COMBAT, true);
        addSetting(velocityMode);
    }

}
