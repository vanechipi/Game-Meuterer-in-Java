package Test;

import static org.junit.Assert.*;

import org.junit.Test;







import dad.Game.Commodity;
import dad.server.IPlayer;
import dad.server.Player;
import dad.server.DBServer;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 	Test de Player
 * 	Comprueba que un jugador sea creado correctamente.
 * 
 */


public class PlayerTest {

	@Test
	public void playerTest() {
		
		String msg="Failure executing 'discardStevedoreTest' method";
		IPlayer player=new Player("player_1");
		
		List<String> commandEnabled = new ArrayList<String>();
		commandEnabled.add("VER_JUGADORES");
		commandEnabled.add("VER_PARTIDAS");
		commandEnabled.add("CREAR_PARTIDA");
		commandEnabled.add("UNIRSE_PARTIDA");
		commandEnabled.add("NUEVO_NOMBRE");
		
		Boolean condition = player.getName() == "player_1" && player.getIsLobby() == true
				&& player.getHand().isEmpty() == true && player.getPoints() == 0 
				&& player.isPassState() == false && player.getCommandEnabled().equals(commandEnabled);
		
		assertTrue(msg,condition);
	
		
 	}

}
