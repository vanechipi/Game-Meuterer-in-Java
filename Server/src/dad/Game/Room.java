package dad.Game;

import java.util.ArrayList;
import java.util.List;

import dad.Command.DbCommandDispatcher;
import dad.Command.GameCommandDispatcher;
import dad.server.IPlayer;
import dad.server.Message;

public class Room {
	private static final Integer limitPlayer=4;
	private String name;
	private static List<IPlayer> players;
	private Game game;
	private DbCommandDispatcher dbCommandDispatcher;

	public Room(String name, DbCommandDispatcher dbCommandDispatcher){
		this.name=name;
		this.players=new ArrayList<IPlayer>();
		this.dbCommandDispatcher = dbCommandDispatcher;
		game=null;
	}
	
	// Unimos a un jugador a una partida
	public synchronized Message joinRoom(IPlayer player){
		Message message = null;
		if(getPlayers().size() < limitPlayer) {
			players.add(player);
			// Sacamos al jugador del lobby
			player.playerLeaveLobby();
			message = new Message(Message.SUCCES_JOINROOM);
			sendMessageToAll(player,new Message("El jugador " + player.getName() + " se ha añadido a la partida"));
			if(getNumberPlayers() == 4) {
				Game game = new Game(players);
				this.game = game;
				changePlayersDispacher();
				Thread thread = new Thread(game);
				thread.start();
			}else {
				player.sendMessage(new Message("Esperando a que se unan más jugadores"));
			}
		}else {
			message = new Message(Message.ERROR_JOIN_FULL);
		}
		return message;
	}
	// una vez que emPieza el juego se cambian los comandos de los jugadores a GameCommandDisPatcher
	private void changePlayersDispacher() {
		GameCommandDispatcher gameCommandDispatcher = new GameCommandDispatcher(this.dbCommandDispatcher, this.game);
		for(IPlayer player : players) {
			player.setCommandDispatcher(gameCommandDispatcher);
		}
	}

	
	//Enviamos mensaje a todos lo jugadores que estan en la partida actual o a uno en concreto
	public static void sendMessageToAll(IPlayer exclude,Message mesg){
		if (exclude==null){
			for (IPlayer p : players) {
				p.sendMessage(mesg);
			}
		}else{
			for (IPlayer p : players) {
				if (exclude!=p)
					p.sendMessage(mesg);
			}
		}
	}
	
	

	//Actualizamos la lista de partidas creadas en el servidor
	public void updateRoomPlayer(IPlayer player){
		player.setRoom(this);
		player.getServer().addRoom(this);             //Isertamos la room a la lista de rooms que tiene el Server.
	}
	
	//numero de jugadores en la partida
	private int getNumberPlayers() {
		return  players.size();
	}

	public String getName(){
		return this.name;
	}
	
	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public List<IPlayer> getPlayers() {
		return players;
	}

	public void setPlayers(List<IPlayer> players) {
		this.players = players;
	}

	public String toString() {
		return "\nNombre: "+this.getName()+" Jugadores: "+this.getPlayers()+" Numero: "+getNumberPlayers()+"/"+limitPlayer;
	}
	public Room(String name){
		this.name=name;
		this.players=new ArrayList<IPlayer>();
		game=null;
	}
	
}
