package peak.modules.misc;

import peak.events.TickEvent;
import peak.modules.Module;
import peak.modules.settings.NumberSetting;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class Spammer extends Module {

    public NumberSetting delay = new NumberSetting("Delay", false, 100, 5000, 1000, 100);

    public Spammer() {
        super("Spammer", 0, Category.MISC, true);
        addSetting(delay);
    }

    private long lastTime;
    String message = "Peak Client on Top! Sub to Pablo client on YT";

    @Override
    public void onEnable() {
        lastTime = System.currentTimeMillis();
        message = "https://i.postimg.cc/tTC1PYjV/received-1636382016789692.gif <@1181342356822294571> <@1238908500474593341>";
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if (tickType == TickEvent.TickType.PRE) {

            if (System.currentTimeMillis() - lastTime >= delay.cValue) {

                int randomNum = java.util.concurrent.ThreadLocalRandom.current().nextInt(100, 1000);
                String finalMessage = message + " [" + randomNum + "]";

                mc.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage(finalMessage));
                lastTime = System.currentTimeMillis();
            }
        }
    }
}