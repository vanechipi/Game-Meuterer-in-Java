package dad.Command;

import dad.Game.Game;
import dad.server.DBServer;


public class GameCommandDispatcher implements CommandDispatcher {
    private DbCommandDispatcher dbCommandDispatcher;
    private Game game;

    public GameCommandDispatcher(DbCommandDispatcher dbCommandDispatcher, Game game) {
        this.dbCommandDispatcher = dbCommandDispatcher;
        this.game = game;
    }

    @Override
    public String send(Command command) {
        if (command instanceof GameCommand) {
            return this.game.execute((GameCommand) command);
        }else if (command instanceof DbCommand) {
            return this.dbCommandDispatcher.send(command);
        }else {
            return command.execute();
        }
    }
}
