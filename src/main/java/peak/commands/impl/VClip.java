package peak.commands.impl;

import peak.commands.Command;

public class VClip extends Command {

    public VClip() {
        super("vclip");
    }

    @Override
    public void onToggle(String[] args) {

        if(args.length > 1) {

            mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + Integer.valueOf(args[1]), mc.thePlayer.posZ);

        }

    }
}
