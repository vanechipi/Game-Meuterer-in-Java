package dad.Command;

import dad.Game.Game;
import dad.server.DBServer;
import dad.server.Player;


public interface CommandDispatcher {
    public String send(Command command);
}
