package dad.Command;

import dad.Game.Game;
import dad.server.Player;


public abstract class GameCommand extends Command {
    public GameCommand(String command, Player player) {
        super(command, player);
    }
    public abstract String execute(Game game);
}
