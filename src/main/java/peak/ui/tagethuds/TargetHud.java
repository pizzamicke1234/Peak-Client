package peak.ui.tagethuds;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import peak.Client;
import peak.managers.font.FontUtil;
import peak.managers.render.RenderManager;
import peak.modules.combat.Killaura;

import java.awt.*;

public class TargetHud extends GuiScreen {

    private static Killaura killaura = (Killaura) Client.getModulebyName("Killaura");
    private static EntityPlayer targetEntity;

    public static void draw() {
        if(killaura.toggled && Killaura.selectedtarget instanceof EntityPlayer) {
            targetEntity = (EntityPlayer) Killaura.selectedtarget;

            String targetName = targetEntity.getName();
            float targetHealth = targetEntity.getHealth();
            int x = 530;
            int y = 230;
            int width = 120;
            int height = 50;

            Color rectColor = new Color(50, 50, 50, 150);
            RenderManager.drawRoundedRect(x, y, width, height, 5, rectColor);

            //Display name of the target
            FontUtil.smaller.drawCenteredString(targetName, x + width / 2, y + 3, -1);

            //Display head of the target
            int offset = 28;
            Gui.drawRect(x + 5, y + 12, x + 5 + offset, y + 12 + offset, 0xAA000000);

            //Display the health of the target
            Color healthColor = new Color(255, 0, 0, 255);
            Color backColor = new Color(30, 30, 30, 150);
            float progress = targetHealth / targetEntity.getMaxHealth();
            RenderManager.drawRoundedRect(x + 5, y + height - 8, (width - 10),  5, 2, backColor);
            RenderManager.drawRoundedRect(x + 5, y + height - 8, (width - 10) * progress,  5, 2, healthColor);

            FontUtil.smaller.drawCenteredString("Health: "+ (int) Math.floor(targetHealth), x + 60, y + 18, -1);

        }
    }

    public static void testdraw() {
        String targetName = "TargetName";
        float targetHealth = 20;
        int x = 370;
        int y = 250;
        int width = 120;
        int height = 50;

        Color rectColor = new Color(50, 50, 50, 150);
        RenderManager.drawRoundedRect(x, y, width, height, 5, rectColor);

        //Display name of the target
        FontUtil.smaller.drawCenteredString(targetName, x + width / 2, y + 3, -1);

        //Display head of the target
        int offset = 28;
        Gui.drawRect(x + 5, y + 12, x + 5 + offset, y + 12 + offset, 0xAA000000);

        //Display the health of the target
        Color healthColor = new Color(255, 0, 0, 255);
        Color backColor = new Color(30, 30, 30, 150);
        float progress = targetHealth / 20;
        RenderManager.drawRoundedRect(x + 5, y + height - 8, (width - 10),  5, 2, backColor);
        RenderManager.drawRoundedRect(x + 5, y + height - 8, (width - 10) * progress,  5, 2, healthColor);

        FontUtil.smaller.drawCenteredString("Health: "+ (int) Math.floor(targetHealth), x + 60, y + 18, -1);
    }

}
