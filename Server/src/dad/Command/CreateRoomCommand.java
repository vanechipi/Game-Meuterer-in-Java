package dad.Command;

import dad.Game.Room;
import dad.server.DBServer;
import dad.server.Message;
import dad.server.Player;

public class CreateRoomCommand extends DbCommand {
	private String name;
	
	public CreateRoomCommand(String command, Player cc) {
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
		Message message;
		
			if (parts.length == 2) {
				name=parts[1];
				
				if(player.getIsLobby()){ //Si está en el lobby
					Room room= server.containsRoom(name);
					
					if(room==null){
						room=new Room(name, new DbCommandDispatcher(server));
						room.getPlayers().add(player);                        //Añade al jugador a la lista de jugadores de la sala que ha creado.
						room.updateRoomPlayer(player);
						player.setRoom(room);
						player.playerLeaveLobby();                             //Sacamos al jugador del lobby
						//Se actualiza comandos permitidos
						player.getCommandEnabled().clear();
						player.getCommandEnabled().add("SALIR");
						
						res=Message.SUCCES_CREATEROOM;
						player.sendMessage(new Message("Esperando a que se una jugadores"));
						message=new Message("El jugador "+ player.getName()+" ha creado una partida con nombre "+name);
						DBServer.sendMessage(player, message, false);
					}else{
						res=Message.ERROR_CREATE_EXISTROOM;
					}
				}else{
					res=Message.ERROR_ISNT_LOBBY;
				}
				
			} else {
				res = "Se esperaban 2 parametros";
			}
		
		return res;
	}
}
