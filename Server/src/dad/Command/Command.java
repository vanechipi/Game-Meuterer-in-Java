package dad.Command;

import dad.server.Player;

public abstract class Command {
	protected String command;
	protected Player player;

	public Command (String command, Player player) {
		this.command = command;
		this.player = player;
	}

	public abstract String execute();
}
