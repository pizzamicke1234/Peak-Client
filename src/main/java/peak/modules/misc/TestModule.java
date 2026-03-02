package peak.modules.misc;

import org.lwjgl.input.Keyboard;
import peak.modules.Module;
import peak.events.TickEvent;
import peak.modules.settings.BoolSetting;
import peak.modules.settings.ModeSetting;

public class TestModule extends Module {

    ModeSetting testMode = new ModeSetting("TestMode", true, "1", "1", "2", "3", "4", "5", "6");

    BoolSetting bool1 = new BoolSetting("Bool1", testMode, new String[]{"1"}, false, false);
    BoolSetting bool2 = new BoolSetting("Bool2", testMode, new String[]{"2"}, false, false);
    BoolSetting bool3 = new BoolSetting("Bool3", testMode, new String[]{"3"}, false, false);
    BoolSetting bool456 = new BoolSetting("Bool456", testMode, new String[]{"4", "5"}, false, false);

    public TestModule() {
        super("TestModule", Keyboard.KEY_J, Category.MISC, true);
        this.addSetting(testMode, bool1, bool2, bool3, bool456);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if (tickType == TickEvent.TickType.POST) return;

    }
}