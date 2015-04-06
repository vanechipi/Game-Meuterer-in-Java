package dad.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import Exceptions.PlayerLeavingGameException;
import dad.Command.Command;
import dad.Command.CommandDispatcher;
import dad.Command.CommandFactory;
import dad.Command.GameCommandDispatcher;
import dad.Game.Commodity;
import dad.Game.Game;
import dad.Game.Room;
import dad.Game.Role;

public class Player implements Runnable,IPlayer {
	Socket clientSocket;
	String ipAddress;
	String name;
	DataInputStream input;
	DataOutputStream output;
	DBServer server;
	Room room; 								// Sala en la que se encuentra
	Boolean isLobby;	 					// Si esta en el lobby o no
	Boolean passState;						// Almacena si el jugador ha elegido pasar o no durante la ronda
	Role character;							// Personaje que tiene asignado
	Integer points;							// Puntos de victoria
	Integer turn;							// turno de la ronda
	List<Commodity> hand; 					// Mano de cartas
	List<String> commandEnabled;			// Lista de comandos permitidos
	CommandDispatcher commandDispatcher;
	
	
	public Player(Socket clientSocket, DBServer server, String name, CommandDispatcher commandDispatcher) {
		this.clientSocket = clientSocket;
		this.server = server;
		this.name = name;
		this.ipAddress = clientSocket.getInetAddress().getHostAddress();
		this.isLobby = true;
		this.hand = new ArrayList<Commodity>();
		this.points = 0;
		this.passState = false ;
		this.commandEnabled = new ArrayList<String>();
		this.commandDispatcher = commandDispatcher;
		commandEnabled.add("VER_JUGADORES");
		commandEnabled.add("VER_PARTIDAS");
		commandEnabled.add("CREAR_PARTIDA");
		commandEnabled.add("UNIRSE_PARTIDA");
		commandEnabled.add("NUEVO_NOMBRE");
	}
	
	public void run() {
		try {
			input = new DataInputStream(clientSocket.getInputStream());
			output = new DataOutputStream(clientSocket.getOutputStream());
			Message message = new Message("Tu nombre es "+name);
			sendMessage(message);
			System.out.println("Creada conexion con jugador "+name+" desde " + ipAddress);
			sendMessage(new Message("LOBBY:\n-NUEVO_NOMBRE\n-VER_JUGADORES\n-VER_PARTIDAS\n-CREAR_PARTIDA nombre\n-UNIRSE_PARTIDA nombre"));
			inputProcessing();
		} catch (PlayerLeavingGameException exception) {
			sendMessage(new Message("Has abandonado la partida,juego finalizado"));
			
		} catch (Exception exception) {
			server.disconnectClient(this);
			
		}
	}

	public void inputProcessing() throws IOException {
		while (clientSocket.isConnected()) {
			String message = input.readUTF();
			Command command = CommandFactory.createCommand(message, this);
			
			if (getRoom() != null) {
				if (getRoom().getGame() != null) {
					getRoom().getGame().receive(this, command);
					
				}
			}
			String info = commandDispatcher.send(command);
			Message msg = new Message(info);
			sendMessage(msg);
		}
	}

	public String execute(Command command) {
		return command.execute();
	}

	public void receiveCards(List<Commodity> cards){
		hand.addAll(cards);
	}
	
	public void sendMessage(Message message) {
		if (clientSocket!=null) {
			if (clientSocket.isConnected()) {
				try {
					if (message != null && output != null) {
						output.writeUTF(message.toString());
					}
				} catch (IOException e) {
				}
			}
		}
	}

	
	public boolean playerLeaveLobby(){
		getServer().removePlayerToLobby(this);
		setIsLobby(false);
		return true;
	}
	
	public boolean close() {
		boolean res = true;
		try {
			clientSocket.close();
		} catch (IOException e) {
			res = false;
		}
		return res;
	}

	public Commodity commodityOfHand(String nameCommodity){
		Commodity card = null;
		nameCommodity=nameCommodity.toUpperCase();
		for(Commodity commodity: getHand()){
			if(commodity.getName().equals(nameCommodity))
				card=commodity;
		}
		return card;
	}

	public String getIp() {
		return ipAddress;
	}
	
	public Role getCharacter() {
		return character;
	}

	public void setCharacter(Role character) {
		this.character = character;
	}

	public Integer getTurn() {
		return turn;
	}

	public void setTurn(Integer turn) {
		this.turn = turn;
	}

	public String getName() {
		return name;
	}

	public Integer getPoints(){
		return points;
	}
	
	public void addPoints(Integer points){
		this.points=points + getPoints();
	}
	
	public void setName(String name) {
		this.name=name;
	}

	public DBServer getServer(){
		return server;
	}
	
	public Boolean getIsLobby(){
		return this.isLobby;
	}

	public void setIsLobby(Boolean isLobby){
		this.isLobby=isLobby;
	}

	public Room getRoom() {
		return room;
	}
	
	public List<String> getCommandEnabled() {
		return commandEnabled;
	}
	
	public void setRoom(Room room) {
		this.room = room;
	}

	public List<Commodity> getHand() {
		return hand;
	}

	public void setHand(List<Commodity> hand) {
		getHand().addAll(hand);
	}

	public boolean isPassState() {
		return passState;
	}

	public void setPassState(boolean pasState) {
		this.passState = pasState;
	}

	public void setCommandDispatcher(CommandDispatcher commandDispatcher) {
		this.commandDispatcher = commandDispatcher;
	}

	public String toString(){
		return "Jugador: "+name;
	}
	
	
	public Player(String name){
		
		this.name = name;
		this.isLobby = true;
		this.hand = new ArrayList<Commodity>();
		this.points = 0;
		this.passState = false ;
		this.commandEnabled = new ArrayList<String>();
		commandEnabled.add("VER_JUGADORES");
		commandEnabled.add("VER_PARTIDAS");
		commandEnabled.add("CREAR_PARTIDA");
		commandEnabled.add("UNIRSE_PARTIDA");
		commandEnabled.add("NUEVO_NOMBRE");
	}
	
}
