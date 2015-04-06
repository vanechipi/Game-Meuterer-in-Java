package dad.Command;

import dad.server.DBServer;
import dad.server.Message;
import dad.server.Player;

import javax.management.openmbean.InvalidOpenTypeException;

public class ShowPlayerCommand extends DbCommand {

	public ShowPlayerCommand(String command, Player cc) {
		super (command,cc);
	}

	@Override
	public String execute()
	{
		throw new InvalidOpenTypeException("This is a game command, 'execute' needs a parameter of type Game");
	}

	@Override
	public String execute(DBServer server) {
		String res = null;
		String [] parts = command.split(" ");
		try{
			if (parts.length == 1) {
				if(player.getIsLobby()){
					res = "Jugadores conectados: "+ server.getLobby();
				}else{
					res=Message.ERROR_ISNT_LOBBY;
				}
			} else {
				res = "Se esperaban 1 parametros";
			}
		} catch (NumberFormatException exc) {
			res = "Clave no numerica";
		}
		return res;
	}
}
