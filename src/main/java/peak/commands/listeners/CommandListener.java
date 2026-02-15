package peak.commands.listeners;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import peak.commands.Command;
import peak.commands.impl.Test;
import peak.commands.impl.VClip;
import peak.events.PacketEvent;
import peak.managers.NotificationManager;

import java.util.concurrent.CopyOnWriteArrayList;

public class CommandListener {

    static CopyOnWriteArrayList<Command> commands = new CopyOnWriteArrayList<>();
    public static final String prefix = ".";

    public static void handle(PacketEvent packetEvent) {
        Packet packet = packetEvent.getPacket();

        if(packet instanceof C01PacketChatMessage) {

            String message = ((C01PacketChatMessage) packet).getMessage().toLowerCase();

            if(message.startsWith(prefix)) {
                toggleCommands(message);
                packetEvent.cancelPacket();
            }

        }

    }

    public static void toggleCommands(String message) {
        for(Command command : commands) {
            if(message.startsWith("." + command.getName())) {
                String[] args = message.split(" ");

                command.onToggle(args);
                return;
            }
        }
        NotificationManager.addChat("§cUnknown Command");
    }

    public static void initCommands() {
        commands.add(new Test());
        commands.add(new VClip());
    }

}
