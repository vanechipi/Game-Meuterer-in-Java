package dad.Command;

import dad.Game.Game;
import dad.Game.Room;
import dad.server.Message;
import dad.server.Player;

public class SalaryBoatswainCommand extends GameCommand {
	private Integer points;

	public SalaryBoatswainCommand(String command, Player cc) {
		super(command, cc);
	}

	@Override
	public String execute()
	{
		throw new UnsupportedOperationException("This is a game command, 'execute' needs a parameter of type Game");
	}

	@Override
	public String execute(Game game) {
		String response = null;
		String[] parts = command.split(" ");

		if (parts.length == 2) {
			
			this.points = new Integer(parts[1]);
			if (!player.getIsLobby()) {
				
				if (points <= 3 || points > 0) { 				// Si está en el rango permitido
					game.setBoatswainPoints(points);
					Room.sendMessageToAll(player,new Message("Sueldo elegido al contramaestre: "+ points));
					response = "Sueldo elegido al contramaestre: " + points;
				}else {
					response = Message.ERROR_CHOOSEN_POINTS;
				}
			}else {
				response = Message.ERROR_ISNT_GAME;
			}
		}else {
			response = "Se esperaban 2 parametros";
		}
		return response;
	}
}
