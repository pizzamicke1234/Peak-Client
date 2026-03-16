package peak.modules.misc;

import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.server.S02PacketChat;
import peak.events.PacketEvent;
import peak.managers.PacketManager;
import peak.modules.Module;

public class AutoLogin extends Module {

    public AutoLogin() {
        super("AutoLogin", 0, Category.MISC, true);
    }

    private final String password = "1234";
    private final C01PacketChatMessage registerPacket = new C01PacketChatMessage("/register " + password + " " + password);
    private final C01PacketChatMessage loginPacket = new C01PacketChatMessage("/login " + password);

    @Override
    public void onPacket(PacketEvent packetEvent) {
        if(packetEvent.getType() == PacketEvent.Type.SEND) return;
        if(!(packetEvent.getPacket() instanceof S02PacketChat)) return;

        S02PacketChat packet = (S02PacketChat) packetEvent.getPacket();
        String message = packet.getChatComponent().getUnformattedText();

        if(message.contains("register") || message.contains("/reg")) {
            PacketManager.sendPacketWithoutEvent(registerPacket);
            return;
        }
        if(message.contains("login") || message.contains("/log")) {
            PacketManager.sendPacketWithoutEvent(loginPacket);
            return;
        }

    }
}
