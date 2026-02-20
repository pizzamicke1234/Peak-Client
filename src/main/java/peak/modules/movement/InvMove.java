package peak.modules.movement;

import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import peak.events.TickEvent;
import peak.modules.Module;

public class InvMove extends Module {

    public InvMove() {
        super("InvMove", 0, Category.MOVEMENT, true);
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if(tickType == TickEvent.TickType.POST) return;

        KeyBinding[] key = { this.mc.gameSettings.keyBindForward, this.mc.gameSettings.keyBindBack, this.mc.gameSettings.keyBindRight, /*this.mc.gameSettings.keyBindSneak,*/ this.mc.gameSettings.keyBindJump, this.mc.gameSettings.keyBindLeft, this.mc.gameSettings.keyBindSprint };
        KeyBinding[] array;
        for(int length = (array = key).length, i = 0; i < length; ++i) {
            KeyBinding b = array[i];
            KeyBinding.setKeyBindState(b.getKeyCode(),
                    Keyboard.isKeyDown(b.getKeyCode()));
        }

    }
}
