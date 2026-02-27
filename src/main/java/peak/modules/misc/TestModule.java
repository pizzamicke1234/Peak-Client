package peak.modules.misc;

import org.lwjgl.input.Keyboard;
import peak.modules.Module;
import peak.events.TickEvent;
import peak.modules.settings.BoolSetting;
import peak.modules.settings.ModeSetting;
import peak.modules.settings.NumberSetting;

public class TestModule extends Module {

    ModeSetting testMode = new ModeSetting("TestMode", true, "1", "1", "2");
    BoolSetting normBool = new BoolSetting("NormBool", false, false);
    BoolSetting testBool = new BoolSetting("TestBool", testMode, "2", false, false);
    ModeSetting test1Mode = new ModeSetting("BoundMode", testMode, "2", true, "1", "1", "2");
    NumberSetting testNum = new NumberSetting("TestNum", testMode, "1", false, 1, 1, 1, 1);

    public TestModule() {
        super("TestModule", Keyboard.KEY_J, Category.MISC, true);
        this.addSetting(testMode, normBool, testBool, test1Mode, testNum);
    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if (tickType == TickEvent.TickType.POST) return;

    }
}