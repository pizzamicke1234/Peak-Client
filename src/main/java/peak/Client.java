package peak;

import org.lwjgl.opengl.Display;
import peak.managers.font.FontUtil;
import peak.modules.Module;
import peak.modules.combat.Killaura;
import peak.modules.combat.Velocity;
import peak.modules.misc.Disabler;
import peak.modules.misc.TestModule;
import peak.modules.movement.Fly;
import peak.modules.movement.Speed;
import peak.modules.movement.Sprint;
import peak.modules.player.ChestStealer;
import peak.modules.player.NoSlow;
import peak.modules.render.Animations;
import peak.modules.render.ClickGuimod;
import peak.modules.render.ESP;
import peak.tickevents.TickEvent;
import peak.viaversion.viamcp.ViaMCP;

import java.util.concurrent.CopyOnWriteArrayList;


public class Client {

    // General settings of the client
    public static String name = "Peak";
    public static String version = "0.6";
    public static CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<Module>();


    public static void startup() {

        System.out.println("Launched " + name + " " + version);
        Display.setTitle(name + " " + version);

        FontUtil.bootstrap();
        setupViaVersion();

        //MISC
        modules.add(new TestModule());
        modules.add(new Disabler());

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

        //PLAYER
        modules.add(new NoSlow());
        modules.add(new ChestStealer());
    }

    public static void setupViaVersion() {
        try {
            ViaMCP.create();
            ViaMCP.INSTANCE.initAsyncSlider(); // For top left aligned slider
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void on_Tick(TickEvent.TickType tickType) {
        for (Module m : modules) {
            if (!m.toggled) {
                continue;
            }
            m.on_Tick(tickType);
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
