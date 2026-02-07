package peak;

import net.minecraft.network.Packet;
import org.lwjgl.opengl.Display;
import peak.events.PacketEvent;
import peak.managers.font.FontUtil;
import peak.modules.Module;
import peak.modules.combat.Killaura;
import peak.modules.combat.Velocity;
import peak.modules.misc.Disabler;
import peak.modules.misc.Phase;
import peak.modules.misc.TestModule;
import peak.modules.movement.Fly;
import peak.modules.movement.Speed;
import peak.modules.movement.Sprint;
import peak.modules.player.ChestStealer;
import peak.modules.player.InvManager;
import peak.modules.player.NoSlow;
import peak.modules.player.Scaffold;
import peak.modules.render.Animations;
import peak.modules.render.Capes;
import peak.modules.render.ClickGuimod;
import peak.modules.render.ESP;
import peak.events.TickEvent;
import peak.viaversion.viamcp.ViaMCP;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;


public class Client {

    // General settings of the client
    public static String name = "Peak";
    public static String version = "0.76";
    public static CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<Module>();

    //Used to detect and show other Users of the Client (doesn't work atm)
    public static Set<UUID> peakUsers = new HashSet<>();

    public static void startup() {

        System.out.println("Launched " + name + " " + version);
        Display.setTitle(name + " " + version);

        FontUtil.bootstrap();
        setupViaVersion();

        //MISC
        modules.add(new TestModule());
        modules.add(new Disabler());
        modules.add(new Phase());

        //MOVEMENT
        modules.add(new Fly());
        modules.add(new Sprint());
        modules.add(new Speed());

        //COMBAT
        modules.add(new Killaura());
        modules.add(new Velocity());

        //RENDER
        modules.add(new ClickGuimod());
        modules.add(new ESP());
        modules.add(new Animations());
        modules.add(new Capes());

        //PLAYER
        modules.add(new NoSlow());
        modules.add(new ChestStealer());
        modules.add(new InvManager());
        modules.add(new Scaffold());

        //Sort the modules by name
        modules.sort(Comparator.comparing(Module::getName));
    }

    public static void setupViaVersion() {
        try {
            ViaMCP.create();
            ViaMCP.INSTANCE.initAsyncSlider(); // For top left aligned slider
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onTick(TickEvent.TickType tickType) {
        for (Module m : modules) {
            if (!m.toggled) {
                continue;
            }
            m.onTick(tickType);
        }
    }

    public static void onPacket(PacketEvent packetEvent) {
        for(Module m : modules) {
            if (!m.toggled) {
                continue;
            }
            m.onPacket(packetEvent);
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
