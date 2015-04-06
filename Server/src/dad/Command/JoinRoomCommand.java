package dad.Command;

import dad.Game.Room;
import dad.server.DBServer;
import dad.server.Message;
import dad.server.Player;

public class JoinRoomCommand  extends DbCommand {
	public String nameRoom;
	
	public JoinRoomCommand(String command, Player player) {
		super (command, player);
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
		try {
			if (parts.length == 2) {
				nameRoom=parts[1];
				// Está en el lobby.
				if(player.getIsLobby()) {
					// ¿Existe una sala con ese nombre?
					Room room = server.containsRoom(nameRoom);
					if(room != null) {
						player.setRoom(room);
						res = player.getRoom().joinRoom(player).getMsg();
						// Se actualiza comandos permitidos
						player.getCommandEnabled().clear();
						player.getCommandEnabled().add("SALIR");
					}else {
						res= Message.ERROR_JOIN_DONTEXISTROOM;
					}
				}else {
					res=Message.ERROR_ISNT_LOBBY;
				}
			}else {
				res = "Se esperaban 2 parametros";
			}
		} catch (NumberFormatException exc) {
			res = "Clave no numerica";
		}
		return res;
	}
}