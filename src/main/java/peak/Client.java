package peak;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S14PacketEntity;
import org.lwjgl.opengl.Display;
import peak.commands.impl.Teleport;
import peak.commands.listeners.CommandListener;
import peak.events.AttackEvent;
import peak.events.PacketEvent;
import peak.events.RenderEvent;
import peak.managers.PacketManager;
import peak.managers.RotationManager;
import peak.managers.font.FontUtil;
import peak.managers.misc.BlinkManager;
import peak.managers.misc.PingSpoofManager;
import peak.modules.Module;
import peak.modules.combat.*;
import peak.modules.fun.*;
import peak.modules.misc.*;
import peak.modules.movement.*;
import peak.modules.player.*;
import peak.modules.player.Timer;
import peak.modules.render.*;
import peak.events.TickEvent;
import peak.viaversion.viamcp.ViaMCP;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;


public class Client {

    /**TODO:
     * -Vanilla fly check bypass *lowkey unmöglich*
     * -toggle sound
     * -teleport command *
     */

    // General settings of the client
    public static final String name = "Peak";
    public static final String version = "0.911";
    public static CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<Module>();

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static final BlinkManager blinkManager = new BlinkManager();
    public static final PingSpoofManager pingSpoofManager = new PingSpoofManager();

    //Used to detect and show other Users of the Client (doesn't work atm)
    public static Set<UUID> peakUsers = new HashSet<>();

    public static void startup() {

        System.out.println("Launched " + name + " " + version);
        Display.setTitle(name + " " + version);

        FontUtil.bootstrap();
        setupViaVersion();
        CommandListener.initCommands();

        //MISC
        modules.add(new TestModule());
        modules.add(new Disabler());
        modules.add(new Phase());
        modules.add(new Spammer());
        modules.add(new Ambience());
        modules.add(new AutoLogin());

        //MOVEMENT
        modules.add(new Fly());
        modules.add(new Sprint());
        modules.add(new Speed());
        modules.add(new Longjump());
        modules.add(new Spider());
        modules.add(new InvMove());
        modules.add(new TargetStrafe());

        //COMBAT
        modules.add(new Killaura());
        modules.add(new Velocity());
        modules.add(new Criticals());
        modules.add(new InfiniteAura());
        modules.add(new Backtrack());

        //RENDER
        modules.add(new ClickGuimod());
        modules.add(new ESP());
        modules.add(new Animations());
        modules.add(new Capes());
        modules.add(new HUDMod());

        //PLAYER
        modules.add(new NoSlow());
        modules.add(new ChestStealer());
        modules.add(new ChestAura());
        modules.add(new InvManager());
        modules.add(new Scaffold());
        modules.add(new AutoPotion());
        modules.add(new Timer());
        modules.add(new Blink());
        modules.add(new FastEat());

        //FUN
        modules.add(new DeathzoneFunny());

        //Sort the modules by name
        modules.sort(Comparator.comparing(Module::getName));
    }

    public static void setupViaVersion() {
        try {
            ViaMCP.create();
            ViaMCP.INSTANCE.initAsyncSlider(); // For top left aligned slider
            ViaMCP.INSTANCE.getAsyncVersionSlider().setVersion(47);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onTick(TickEvent.TickType tickType) {

        if(tickType == TickEvent.TickType.PRE) {
            pingSpoofManager.onTick();
        }

        for (Module m : modules) {
            if (!m.toggled) {
                continue;
            }
            m.onTick(tickType);
        }
    }

    public static void onPacket(PacketEvent packetEvent) {

        if (PacketManager.packetsWithoutEvent.contains(packetEvent.getPacket())) {
            PacketManager.packetsWithoutEvent.remove(packetEvent.getPacket());
            return;
        }

        CommandListener.handle(packetEvent);

        if (packetEvent.getType() == PacketEvent.Type.SEND) {
            blinkManager.onPacketSend(packetEvent);
        }

        if (packetEvent.isCanceled()) return;

        pingSpoofManager.handlePacket(packetEvent);

        if(!Teleport.allowC03Packets && packetEvent.getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition) {
            packetEvent.cancelPacket();
        }

        if (packetEvent.isCanceled()) return;

        for(Module m : modules) {
            if (!m.toggled) {
                continue;
            }
            m.onPacket(packetEvent);
        }
    }

    public static void onRender(RenderEvent renderEvent) {
        for(Module m : modules) {
            if (!m.toggled) {
                continue;
            }
            m.onRender(renderEvent);
        }
    }

    public static void onAttack(AttackEvent attackEvent) {
        for(Module m : modules) {
            if (!m.toggled) {
                continue;
            }
            m.onAttack(attackEvent);
        }
    }

    public static void on_keyPress(int key) {
        for (Module m : modules) {
            if (m.key == key) {
                m.toggle();
            }
        }
    }

    public static Module getModulebyName(String name) {
        for (Module m : modules) {
            if (m.name.equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

}
