package dad.Command;

import dad.server.DBServer;
import dad.server.Player;


public abstract class DbCommand extends Command {
    public DbCommand(String command, Player player) {
        super(command, player);
    }

    public abstract String execute(DBServer server);
}
