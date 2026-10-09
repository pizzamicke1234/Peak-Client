package peak.commands.listeners;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import peak.commands.Command;
import peak.commands.impl.*;
import peak.events.PacketEvent;
import peak.ui.notifications.NotificationManager;

import java.util.concurrent.CopyOnWriteArrayList;

public class CommandListener {

    static CopyOnWriteArrayList<Command> commands = new CopyOnWriteArrayList<>();
    public static final String prefix = ".";

    public static void handle(PacketEvent packetEvent) {
        Packet packet = packetEvent.getPacket();

        if(packet instanceof C01PacketChatMessage) {

            String message = ((C01PacketChatMessage) packet).getMessage();

            if(message.startsWith(prefix)) {
                toggleCommands(message);
                packetEvent.cancelPacket();
            }

        }

    }

    public static void toggleCommands(String message) {
        for(Command command : commands) {
            if(message.toLowerCase().startsWith("." + command.getName())) {
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
        commands.add(new Bind());
        commands.add(new Teleport());
        commands.add(new Config());
    }

}
