package peak;

import com.sun.org.apache.xpath.internal.operations.Mod;
import org.lwjgl.opengl.Display;
import peak.modules.Module;
import peak.modules.combat.Killaura;
import peak.modules.movement.Fly;
import peak.modules.movement.Speed;
import peak.modules.movement.Sprint;
import peak.modules.render.ClickGuimod;

import java.util.concurrent.CopyOnWriteArrayList;

public class Client {

    // General settings of the client
    public static String name = "Peak";
    public static String version = "0.1";
    public static CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<Module>();


    public static void startup() {

        System.out.println("Launched " + name + " " + version);
        Display.setTitle(name + " " + version);

        modules.add(new Fly());
        modules.add(new Sprint());
        modules.add(new Speed());

        modules.add(new Killaura());

        modules.add(new ClickGuimod());
    }

    public static void on_Tick() {
        for(Module m : modules) {
            if(!m.toggled) {
                continue;
            }
            m.on_Tick();
        }
    }

    public static void on_keyPress(int key) {
        for(Module m : modules) {
            if(m.key == key) {
                m.toggle();
            }
        }
    }

}
