package peak.modules.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.world.WorldProvider;
import org.lwjgl.input.Keyboard;
import peak.events.PacketEvent;
import peak.managers.DamageManager;
import peak.managers.PacketManager;
import peak.managers.render.HitBox;
import peak.managers.render.RenderManager;
import peak.modules.Module;
import peak.events.TickEvent;
import peak.ui.notifications.Notification;
import peak.ui.notifications.NotificationManager;

import java.util.ArrayList;

public class TestModule extends Module {

    ArrayList<String> foundCommands = new ArrayList<>();

    private char[] currentChars;
    private int currentLength = 1;
    private int tickDelay = 0;
    private final int MAX_LENGTH = 4;

    private final String alphabet = "abcdefghijklmnopqrstuvwxyz";

    public TestModule() {
        super("TestModule", Keyboard.KEY_J, Category.MISC, true);
        resetBruteForce();
    }

    private void resetBruteForce() {
        currentChars = new char[currentLength];
        for (int i = 0; i < currentLength; i++) currentChars[i] = 'a';
    }

    @Override
    public void onEnable() {
        resetBruteForce();
    }

    @Override
    public void onTick(TickEvent.TickType tickType) {
        if (tickType == TickEvent.TickType.POST) return;

        if(mc.thePlayer.ticksExisted % 4 == 0) {
            sendNextCommand();
        }
    }

    private void sendNextCommand() {
        String cmd = new String(currentChars);

        if(!cmd.equalsIgnoreCase("lobby") && !cmd.equalsIgnoreCase("hub")) {
            mc.thePlayer.sendQueue.addToSendQueue(new C01PacketChatMessage("/" + cmd));
        }

        // Calculate the next combination
        if (!incrementState()) {
            if (currentLength < MAX_LENGTH) {
                currentLength++;
                resetBruteForce();
            } else {
                this.toggle();
            }
        }
    }

    private boolean incrementState() {
        int i = currentChars.length - 1;
        while (i >= 0) {
            if (currentChars[i] < 'z') {
                currentChars[i]++;
                return true;
            }
            currentChars[i] = 'a';
            i--;
        }
        return false;
    }

    @Override
    public void onPacket(PacketEvent packetEvent) {
        if (packetEvent.getType() == PacketEvent.Type.SEND) return;
        Packet packet = packetEvent.getPacket();

        if (packet instanceof S02PacketChat) {
            String message = ((S02PacketChat) packet).getChatComponent().getUnformattedText();

            if (!message.contains("Unknown command") && !message.contains("/help")) {

                Notification notification = new Notification("Command", message, Notification.NotificationType.INFO, 5000L);
                NotificationManager.addNotification(notification);
                foundCommands.add(message);
            }
        }
    }
}