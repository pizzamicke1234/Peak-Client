package peak.modules.render;

import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.shader.ShaderGroup;
import org.lwjgl.input.Keyboard;
import peak.modules.Module;

public class ESP extends Module {

    public ESP() {
        super("ESP", Keyboard.KEY_NONE, Category.RENDER, true);
    }

    public ShaderGroup entityOutlineShader = RenderGlobal.entityOutlineShader;

}
