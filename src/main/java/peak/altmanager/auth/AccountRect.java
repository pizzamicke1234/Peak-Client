package peak.altmanager.auth;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.Session;
import peak.managers.font.FontUtil;
import peak.managers.render.RenderManager;
import peak.ui.mainmenus.elements.PeakButton;

import java.awt.*;

/**This is the rect used to represent the accounts saved in the altmanager
 * Lowk peak code
 */

public class AccountRect extends Gui {

    Session accountSession;
    int x, y, width, height;
    Color color;

    public AccountRect(Session accountSession, int x, int y, int width, int height, Color color) {
        this.accountSession = accountSession;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public Session getAccountSession() {
        return accountSession;
    }

    public void draw() {
        RenderManager.drawRoundedRect(x, y, width, height, 1, color);
        FontUtil.normal.drawCenteredString(accountSession.getUsername(), x + width / 2, y + 5, -1);
    }

}
