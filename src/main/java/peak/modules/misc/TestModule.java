package peak.modules.misc;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import peak.modules.Module;
import peak.modules.settings.BoolSetting;
import peak.modules.settings.ModeSetting;

public class TestModule extends Module {

    ModeSetting Mode = new ModeSetting("Mode", true, "Test", "Test", "Test1", "Test2", "Test3");
    ModeSetting Mode1 = new ModeSetting("Mode1", true, "Test", "Test", "Test1", "Test2", "Test3");
    BoolSetting Testbool = new BoolSetting("Testbool", true, false);

    public TestModule() {
        super("TestModule", Keyboard.KEY_J, Category.MISC, true);
        addSetting(Mode, Mode1, Testbool);
    }

    @Override
    public void on_Enable() {
        Mode.nextMode();
    }

    @Override
    public void on_Disable() {
        System.out.println("TestModule was Disabled!");
    }

}
