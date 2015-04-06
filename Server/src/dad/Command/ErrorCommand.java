package dad.Command;

import dad.server.Player;

public class ErrorCommand extends Command {
	public ErrorCommand (String command, Player cc) {
		super(command,cc);
	}
	
	@Override
	public String execute() {
		return command;
	}
}
