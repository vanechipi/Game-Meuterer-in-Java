package dad.Command;

import dad.server.DBServer;
import dad.server.Message;
import dad.server.Player;

public class ChangeNamePlayerCommand extends DbCommand{
	public String nameRoom;

	public ChangeNamePlayerCommand(String command, Player cc) {
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
		String oldName,newName;

		if (parts.length == 2) {
			newName=parts[1];
			if(!server.existPlayerName(newName)){
				oldName= player.getName();
				player.setName(newName);
				res="Tu nuevo nombre de jugador es "+ player.getName();
				Message message = new Message("El jugador "+ oldName + " ahora se llama "+ player.getName());
				DBServer.sendMessage(player, message, false); //modificacion
			}else{
				   res="El nombre "+parts[1]+" ya esta siendo usado por otro jugador, tu nombre sigue siendo "+ player.getName();
			}

		} else {
			res = "Se esperaban 2 parámetros";
		}

		return res;
	}
}
