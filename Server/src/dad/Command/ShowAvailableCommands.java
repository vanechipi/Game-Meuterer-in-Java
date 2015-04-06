package dad.Command;

import dad.server.Player;

public class ShowAvailableCommands extends Command {
	public ShowAvailableCommands(String command, Player cc) {
		super(command,cc);
	}
	
	@Override
	public String execute() {
		return player.getCommandEnabled().toString();
	}
}