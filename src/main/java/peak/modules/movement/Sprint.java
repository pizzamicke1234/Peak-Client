package peak.modules.movement;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import peak.Client;
import peak.modules.Module;
import peak.modules.player.Scaffold;
import peak.modules.settings.ModeSetting;
import peak.events.TickEvent;


public class Sprint extends Module {

    ModeSetting sprintmode = new ModeSetting("Mode",false,  "Legit", "Legit", "Omnisprint");

    public Sprint() {
        super("Sprint", Keyboard.KEY_NONE, Category.MOVEMENT, true);
        addSetting(sprintmode);
        toggled = true;
    }

    public void onEnable() {

    }

    public void onDisable() {

    }

    public void onTick(TickEvent.TickType tickType) {

        if(tickType == TickEvent.TickType.POST) return;

        switch (sprintmode.currentValue) {
            case "Legit":
                int keyCode = mc.gameSettings.keyBindSprint.getKeyCode();
                KeyBinding.setKeyBindState(keyCode, true);
                //KeyBinding.setKeyBindState(keyCode, false);
                break;
            case "Omnisprint":
                mc.thePlayer.setSprinting(true);
                break;
        }
    }

}
