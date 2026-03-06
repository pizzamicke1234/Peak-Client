package peak.modules.misc;

import org.lwjgl.input.Keyboard;
import peak.events.TickEvent;
import peak.modules.Module;
import peak.modules.settings.BoolSetting;
import peak.modules.settings.ModeSetting;

public class Phase extends Module {

    private ModeSetting phaseMode = new ModeSetting("Mode", true, "Clip", "Clip");

    private BoolSetting sneakClip = new BoolSetting("On Sneak", phaseMode, new String[]{"Clip"}, false, false);

    public Phase() {
        super("Phase", Keyboard.KEY_U, Category.MISC, true);
        addSetting(phaseMode, sneakClip);
    }

    @Override
    public void onEnable() {

        if(phaseMode.currentValue == "Clip") {
            if(!sneakClip.isTrue()) {
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 3, mc.thePlayer.posZ);
                this.toggle();
            }
        }

    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if(phaseMode.currentValue == "Clip") {
            if(sneakClip.isTrue()) {
                if(mc.gameSettings.keyBindSneak.isPressed()) {
                    mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY - 3, mc.thePlayer.posZ);
                }
            }
        }
    }
}
