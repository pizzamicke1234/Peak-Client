package peak.managers.file.altmanager;

import net.minecraft.util.Session;

public class SessionSave {

    public String name, playerID, token, type;

    public SessionSave(Session session) {
        this.name = session.getUsername();
        this.playerID = session.getPlayerID();
        this.token = session.getToken();
        this.type = String.valueOf(session.getSessionType());
    }
}
