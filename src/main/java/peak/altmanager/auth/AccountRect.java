package peak.altmanager.auth;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import peak.managers.font.FontUtil;
import peak.managers.render.RenderManager;

import java.awt.*;

/**This is the rect used to represent the accounts saved in the altmanager
 * Lowk peak code
 */

public class AccountRect extends Gui {

    Session accountSession;
    ResourceLocation sessionSkin;
    boolean selected;

    public AccountRect(Session accountSession) {
        this.accountSession = accountSession;
        this.sessionSkin = RenderManager.getSessionSkin(accountSession);
    }

    public Session getAccountSession() {
        return accountSession;
    }

    public void draw(int x, int y, int width, int height, Color color) {
        RenderManager.drawRoundedRect(x, y, width, height, 1, color);
        if(Minecraft.getMinecraft().session == accountSession) {
            RenderManager.drawRoundedRect(x, y, width, height, 1, new Color(145, 255, 142, 68));
        }else if(selected) {
            RenderManager.drawRoundedRect(x, y, width, height, 1, new Color(150, 150, 150, 100));
        }
        FontUtil.bigger.drawCenteredString(accountSession.getUsername(), x + width / 2, y + 5, -1);

        //Session Type
        String sessionType = (accountSession.getSessionType() == Session.Type.MOJANG) ? "Premium" : "Cracked";
        FontUtil.smaller.drawCenteredString(sessionType, x + width / 2, y + height - 5 - FontUtil.smaller.getHeight(), -1);

        //Skin
        //Gui.drawRect(x + 10, y + 5, x + height, y + height - 5, 0xFF000000);
        RenderManager.drawPlayerHead(sessionSkin, x + 10, y + 5, height - 10);
    }

    public void login() {
        Minecraft.getMinecraft().session = accountSession;
    }

    public void onClick() {
        if(!selected) {
            selected = true;
            return;
        }
        if(Minecraft.getMinecraft().session != accountSession) {
            login();
        }
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
