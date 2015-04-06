package dad.Command;

import dad.server.DBServer;
import dad.server.Message;
import dad.server.Player;

public class ShowMatchCommand extends DbCommand {
	public ShowMatchCommand(String command, Player cc) {
		super (command,cc);
		
	}

	@Override
	public String execute()
	{
		throw new UnsupportedOperationException("This is a game command, 'execute' needs a parameter of type Game");
	}

	@Override
	public String execute(DBServer server) {
		String res = null;
		String [] parts = command.split(" ");
		
			if (parts.length == 1) {
				if(player.getIsLobby()){
					res = "Salas creadas:" + server.getRooms();
				}else{
					res= Message.ERROR_ISNT_LOBBY;
				}
			} else {
				res = "Se esperaban 1 parametros";
			}
		return res;
	}
}
