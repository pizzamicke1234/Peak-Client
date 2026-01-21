package peak.modules.misc;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import peak.modules.Module;
import peak.modules.settings.ModeSetting;

public class TestModule extends Module {

    ModeSetting Testmode = new ModeSetting("Testmode", "Test", true, "Test", "Test1", "Test2", "Test3");

    public TestModule() {
        super("TestModule", Keyboard.KEY_J, Category.MISC, true);
        addSetting(Testmode);
    }

    @Override
    public void on_Enable() {
        System.out.println("TestModule was enabled!");
        System.out.println(Testmode.current_value + " ; " + Testmode.modes);
        Testmode.nextMode();
    }

    @Override
    public void on_Disable() {
        System.out.println("TestModule was Disabled!");
    }

}
