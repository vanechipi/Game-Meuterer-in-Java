package dad.Command;

import dad.Game.Room;
import dad.server.DBServer;
import dad.server.Message;
import dad.server.Player;

public class LeaveRoomCommand extends DbCommand {
	public LeaveRoomCommand(String command, Player player) {
		super(command,player);
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
			if (parts.length == 1) {
				if(!player.getIsLobby()) {
					
					Room room = player.getRoom();	
					
					room.getPlayers().remove(player);		
					server.addPlayerToLobby(player);
					
					//Reseteamos atributos de la partida pertenecientes al jugador
					player.setIsLobby(true);
					player.setRoom(null);
					player.setCharacter(null);
					player.getHand().clear();
					player.setPassState(false);
					player.setTurn(0);
					
					player.getCommandEnabled().clear();
					player.getCommandEnabled().add("VER_JUGADORES");
					player.getCommandEnabled().add("VER_PARTIDAS");
					player.getCommandEnabled().add("CREAR_PARTIDA");
					player.getCommandEnabled().add("UNIRSE_PARTIDA");
					player.getCommandEnabled().add("NUEVO_NOMBRE");
					
					if((room.getGame()!=null && room.getPlayers().size()==3)|| room.getPlayers().size()==0){ //Si el juego ya habia empezado, es decir,si habia 4 jugadores, se elimina la partida.
						//O si la sala queda vacia , se elimina.
						DBServer.sendMessage(null, new Message("La sala con nombre '"+room.getName()+"' se ha eliminado."),false);
						server.removeRoom(room);
					} //sino se sigue esperando a que se unan jugadores para empezar
						
					
					
					res = Message.SUCCES_LEAVEROOM;
					
				}else {
					res= Message.ERROR_LEAVE_ISNTROOM;
				}
			}else {
				res = "Se esperaban 1 parametro";
			}
		}catch(NumberFormatException exc) {
			res = "Clave no numerica";
		}
		return res;
	}
}
