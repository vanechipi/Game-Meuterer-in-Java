package dad.Command;

import dad.Game.Game;
import dad.Game.Room;
import dad.server.Message;
import dad.server.Player;

public class PassCommand extends GameCommand {
	public PassCommand (String command, Player cc) {
		super(command,cc);
	}

	@Override
	public String execute()
	{
		throw new UnsupportedOperationException("This is a game command, 'execute' needs a parameter of type Game");
	}

	@Override
	public String execute(Game game) {
		String message=null;
		
		if(!player.getIsLobby()) {
			if( game!=null ) {
				Room.sendMessageToAll(player, new Message(player+" ha pasado."));
				message = Message.SUCCES_PASS;
			}else {
				message = Message.ERROR_ISNT_GAME;
			}
		}else {
			message = Message.ERROR_ISNT_GAME;
		}
		return message;
	}
}
