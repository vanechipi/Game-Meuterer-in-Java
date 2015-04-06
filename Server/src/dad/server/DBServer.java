package dad.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dad.Command.CommandDispatcher;
import dad.Command.DbCommand;
import dad.Command.DbCommandDispatcher;
import dad.Game.Lobby;
import dad.Game.Room;

public class DBServer implements Runnable {
	private ServerSocket ss;
	private static final int port = 6000; 
	private static Lobby lobby;
	private  List<Room> rooms;
	private static List<Player> listPlayers;
	private CommandDispatcher commandDispatcher;

	public DBServer() {
		lobby = new Lobby();
		this.rooms= new ArrayList<Room>();
		listPlayers= new ArrayList<Player>();
		commandDispatcher = new DbCommandDispatcher(this);
	}
	
	public void run() {
		try {
			ss = new ServerSocket(port);
			System.out.println("El Servidor del juego AMOTINADO se iniciado en el puerto " + port);
			while (true) {
				Socket clientSocket = ss.accept();
				createPlayer(clientSocket);
			}
		} catch (IOException e) {
			System.out.println("Fallo al intentar arrancar el servidor");
		}
	}

	public String execute(DbCommand command)
	{
		return command.execute(this);
	}

	private void createPlayer(Socket clientSocket) {
		Player player = new Player(clientSocket, this, newPlayerName(), commandDispatcher);
		listPlayers.add(player);
		lobby.addPlayerToLobby(player);
		System.out.println("Creado jugador: " + player.name + " y metido en la sala comun");
		new Thread(player).start();
		Message message = new Message("Nuevo jugador " + player.getName() + " conectado");
		sendMessage(player, message, false);
	}
	
    private String newPlayerName(){
    	String name="";
    	boolean exit = true;
    	while (exit) {
    		Random rand = new Random();
        	name = "Player_"+rand.nextInt(9999);
        	exit = existPlayerName(name);
    	}
    	return name;
    }
    
	public Boolean existPlayerName(String name){
		Boolean result=false;
		for (Player p: listPlayers){
    		if (p.getName().compareToIgnoreCase(name)==0){
    			result=true;
    		}
    	}
    	return result;
    }
    
	public static void sendMessage(Player source, Message message, boolean onlyPlayer) {
		if (onlyPlayer) {
			source.sendMessage(message);
		}else {
			if (source!=null && !source.isLobby) {
				for (Player dest : lobby.showPlayer()) {
					if (dest != source) {
						dest.sendMessage(message);
					}
				}
			}else {
				for (Player dest : lobby.showPlayer()) {
					if (dest != source) {
						dest.sendMessage(message);
					}
				}
			}
		}
	}
	
	public void disconnectClient(Player clientConnection) {
		if(containsPlayerAnyMatch(clientConnection)){
			clientConnection.getRoom().getPlayers().remove(clientConnection); // Sacamos al jugador de la partida
		}
		lobby.showPlayer().remove(clientConnection);
		listPlayers.remove(clientConnection);
		System.out.println("Jugador " + clientConnection.name + " desconectado");
		Message message = new Message("El jugador "+clientConnection.name + " se ha desconectado");
		sendMessage(null,message,false);
	}
	
	public boolean closeServer() {
		for (Player clients: lobby.showPlayer()) {
			clients.close();
		}
		lobby.showPlayer().clear();
		
		try{
			ss.close();
		}catch(IOException e){
			return true;
		}
		return true;
	}
	
	public Boolean addRoom(Room room) {	
		return rooms.add(room);
	}

	public  Boolean removeRoom(Room room){
		return rooms.remove(room);
	}

	public void removePlayerToLobby(Player player) {
		lobby.showPlayer().remove(player);
	}

	public void addPlayerToLobby(Player player){
		lobby.addPlayerToLobby(player);
	}
	
	public Room containsRoom(String nameMatch){
		Room room=null;
		for(Room r:rooms){
			if(r.getName().equals(nameMatch))
				room=r;
		}
		return room;
	}
	
	public Boolean containsPlayerAnyMatch(Player player){
		Boolean contains=false;
		for(Room r:rooms){
			if(r.getPlayers().contains(player)){
				contains=true;
				break;
			}
		}
		return contains;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public Lobby getLobby(){
		return lobby;
	}
}
