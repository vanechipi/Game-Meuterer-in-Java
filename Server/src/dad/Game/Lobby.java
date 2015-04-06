package dad.Game;


import java.util.ArrayList;
import java.util.List;

import dad.server.IPlayer;
import dad.server.Player;

public class Lobby {
	static List<Player> listPlayers;
	private Integer limitPlayer;
	
	public Lobby() {
		listPlayers = new ArrayList<Player>();
		limitPlayer = 100;
	}
	
	public List<Player> showPlayer(){
		return listPlayers;
	}

	public synchronized Boolean addPlayerToLobby(Player player) {
		Boolean res = false;
		if(!isFull()){
			listPlayers.add(player);
			res = true;
		}
		return res;
	}

	public Boolean isFull(){
		return (limitPlayer.compareTo(listPlayers.size()-1)<0);
	}

	public String toString(){
		return "Lista de jugadores en lobby: "+listPlayers.toString();
	}
}
