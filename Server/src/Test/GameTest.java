/**
 * 
 */
package Test;

import static org.junit.Assert.*;

import org.junit.Test;

import dad.Command.ChooseCardCommand;
import dad.Command.ChooseRoleCommand;
import dad.Command.DiscardStevedoreCommand;
import dad.Command.PassCommand;
import dad.Command.SalaryBoatswainCommand;
import dad.Game.CardType;
import dad.Game.Commodity;
import dad.Game.Role;
import dad.Game.Room;
import dad.Game.Game;
import dad.server.IPlayer;
import dad.server.Message;
import dad.server.Player;

//import org.mockito.Mock;
//import org.mockito.Mockito;



import java.util.ArrayList;
import java.util.List;

/**
 * 	Test de Game.
 * 	Metodos testeados:
 * 		- createGameTest :  Crear un juego
 * 		- distributeCardTest : Repartir cartas
 *		- getPlayerWhithRoleTest: Jugador que contiene un rol determinado
 *		- numberConflictCardPlayerTest: Numero de cartar conflicto de un jugador determinado
 *		- chooseCommodityCardTest: Jugador vende una mercancia
 *		- chooseRoleTest: Jugador elige un rol
 *		- discardStevedoreTest: Estibado se descarta de 3 cartas
 *		- passStateTest: elegir pasar
 *		- SalaryBoatswainTest: elegir sueldo contramaestre
 */


public class GameTest {
	
	
	@Test
    public void createGameTest() {
 		String msg = "Failure executing 'startGame()' method ";
 		Boolean condition = false;

 		Game game = createGame();

 		condition= game.getNumberPlayers()==4 && game.getPositionActiveIsland()==0 && game.getPositionPreviousIsland()==-1
 				&& game.getRound()==1 && game.isWait==false && game.getPositionCaptainDestinationCard()==0 
 				&& game.getPositionMutineerDestinationCard()==0;
 		
 		assertTrue(msg, condition);
	}
	
	@Test
	public void distributeCardTest() {
		 
		 String msg = "Failure executing 'distributeCard' method";
		 Game game = createGame();
		 game.distributeCards();
		 Boolean condition = true;
		 for(IPlayer player: game.getSortedPlayers()){
			 if(player.getHand().size()!=5)
				 condition=false;
		 }
		 assertTrue(msg,condition);
		 
		 
		 
	 }
	
	
	
	@Test
	public void getPlayerWhithRoleTest(){
		
		String msg = "Failure executing 'getPlayerWhithRoleTest' method";
		 
		Game game = createGame();
		IPlayer player=game.getSortedPlayers().get(0);
		Role role=new Role("CAPITAN",CardType.Role.CAPITAN,CardType.Positioning.AFAVOR);
		
		player.setCharacter(role);
		IPlayer playerWithRole=game.getPlayerWhithRole("CAPITAN");
		
		Boolean condition= player.equals(playerWithRole) && playerWithRole.getCharacter().getName().equals("CAPITAN");
		assertTrue(msg,condition);
		
	}
	
	@Test
	public void numberConflictCardPlayerTest(){
		
		String msg = "Failure executing 'numberConflictCardPlayerTest' method";
		 
		Game game = createGame();
		IPlayer player=game.getSortedPlayers().get(0);
		List<Commodity> hand = new ArrayList<Commodity>();
		
		hand.add(new Commodity("CONFLICTO",CardType.Commodity.CONFLICTO,"DERECHA"));
		hand.add(new Commodity("CONFLICTO",CardType.Commodity.CONFLICTO,"DERECHA"));
		hand.add(new Commodity("CONFLICTO",CardType.Commodity.CONFLICTO,"DERECHA"));
		hand.add(new Commodity("TRIGO",CardType.Commodity.TRIGO,"DERECHA"));
		hand.add(new Commodity("TRIGO",CardType.Commodity.TRIGO,"DERECHA"));
		
		player.setHand(hand);
		
		Integer numberConflictCard=game.numberConflictCardPlayer(player);
		
		Boolean condition= numberConflictCard==3;
		assertTrue(msg,condition);
		
		
	}
	@Test
	public void chooseCommodityCardTest(){
		
		String msg="Failure executing 'chooseCommodityCardTest' method";
		Room room=new Room("My Room");
		Game game= createGame();
		room.setGame(game);
		game.setPositionActiveIsland(3);
		Player player=(Player)game.getSortedPlayers().get(0);
		List<Commodity> hand = new ArrayList<Commodity>();
		hand.add(new Commodity("TRIGO",CardType.Commodity.TRIGO,"DERECHA"));
		hand.add(new Commodity("VINO",CardType.Commodity.VINO,"DERECHA"));
		hand.add(new Commodity("RUBI",CardType.Commodity.RUBI,"DERECHA"));
		hand.add(new Commodity("TELAS",CardType.Commodity.TELAS,"DERECHA"));
		hand.add(new Commodity("TELAS",CardType.Commodity.TELAS,"DERECHA"));
		
		player.setHand(hand);
		
		ChooseCardCommand command= new ChooseCardCommand("ELEGIR_CARTA LENGUA_CHICA TRIGO",player); 
		String result=command.execute(game);
		
		Boolean condition = result==Message.SUCCES_CHOOSE_CARD && player.getHand().size()==4 
				&& game.getDiscard().getCommodityCards().size()==1;
		
		assertTrue(msg,condition);
		
	}
	
	@Test
	public void chooseRoleTest(){
		
		String msg="Failure executing 'chooseRoleTest' method";
		Room room=new Room("My Room");
		Game game= createGame();
		room.setGame(game);
		game.setPositionActiveIsland(3);
		Player player=(Player)game.getSortedPlayers().get(0);
		
		ChooseRoleCommand command= new ChooseRoleCommand("ELEGIR_ROL AMOTINADO",player); 
		String result=command.execute(game);
		
		Boolean condition = result==Message.SUCCES_CHOOSE_CARD 
				&& player.getCharacter().getName().equals("AMOTINADO") 
				&& game.getDeck().getRoleCards().size()==5;
		
		assertTrue(msg,condition);
		
	}
	
	@Test
	public void discardStevedoreTest(){
		
		String msg="Failure executing 'discardStevedoreTest' method";
		Game game= createGame();
		Player player=(Player)game.getSortedPlayers().get(0);
		Role role=new Role("ESTIBADOR",CardType.Role.ESTIBADOR,CardType.Positioning.NEUTRAL);
		player.setCharacter(role);
		List<Commodity> hand = new ArrayList<Commodity>();
		hand.add(new Commodity("TRIGO",CardType.Commodity.TRIGO,"DERECHA"));
		hand.add(new Commodity("VINO",CardType.Commodity.VINO,"DERECHA"));
		hand.add(new Commodity("RUBI",CardType.Commodity.RUBI,"DERECHA"));
		hand.add(new Commodity("TELAS",CardType.Commodity.TELAS,"DERECHA"));
		hand.add(new Commodity("TELAS",CardType.Commodity.TELAS,"DERECHA"));
		hand.add(new Commodity("RUBI",CardType.Commodity.RUBI,"DERECHA"));
		hand.add(new Commodity("RUBI",CardType.Commodity.RUBI,"DERECHA"));
		hand.add(new Commodity("TELAS",CardType.Commodity.TELAS,"DERECHA"));
		player.setHand(hand);
		
		DiscardStevedoreCommand command= new DiscardStevedoreCommand("DESCARTAR TRIGO VINO RUBI",player); 
		String result=command.execute(game);
		
		Boolean condition = result==Message.SUCCES_CHOOSE_CARD && player.getHand().size()==5 
				&& game.getDiscard().getCommodityCards().size()==3;
		
		assertTrue(msg,condition);
		
	}
	
	@Test
	public void passStateTest(){
		
		String msg="Failure executing 'passStateTest' method";
		Game game= createGame();
		Player player=(Player)game.getSortedPlayers().get(0);
		
		PassCommand command= new PassCommand("PASAR",player); 
		String result=command.execute(game);
		game.isWait=true;
		game.receive(player, command);
		
		Boolean condition = result==Message.SUCCES_PASS && player.isPassState()==true;
		
		assertTrue(msg,condition);
	}
	
	@Test
	public void SalaryBoatswainTest(){
		
		String msg="Failure executing 'SalaryBoatswainTest' method";
		Game game= createGame();
		Player player=(Player)game.getSortedPlayers().get(0);
		
		SalaryBoatswainCommand command= new SalaryBoatswainCommand("SUELDO_CONTRAMAESTRE 2",player); 
		String result=command.execute(game);
		
		Boolean condition = result.equals("Sueldo elegido al contramaestre: 2") && game.getBoatswainPoints()==2;
		
		assertTrue(msg,condition);
	}
	
	
	
	
	public Game createGame() {

//	 		DBServer server=new DBServer();
//	 		
//	 		DbCommandDispatcher dbCommandD=new DbCommandDispatcher(server);
//	 		//Room room=new Room("test",dbCommandD);
//	 		Socket clientSocket= Mockito.mock(Socket.class);

	 		List<IPlayer> players = new ArrayList<IPlayer>();
//	 		IPlayer player_1=new Player(clientSocket,server,"Jugador 1", dbCommandD);
//	 		IPlayer player_2=new Player(clientSocket,server,"Jugador 2", dbCommandD);
//	 		IPlayer player_3=new Player(clientSocket,server,"Jugador 3", dbCommandD);
//	 		IPlayer player_4=new Player(clientSocket,server,"Jugador 4", dbCommandD);
	 	
	 		IPlayer player_1=new Player("jugador 1");
	 		IPlayer player_2=new Player("jugador 2");
	 		IPlayer player_3=new Player("jugador 3");
	 		IPlayer player_4=new Player("jugador 4");
	 		
	 		player_1.setIsLobby(false);
	 		player_2.setIsLobby(false);
	 		player_3.setIsLobby(false);
	 		player_4.setIsLobby(false);
	 		
	 		players.add(player_1);
	 		players.add(player_2);
	 		players.add(player_3);
	 		players.add(player_4);

	 		
	 		Game game = new Game(players);
	 		
	 		return game;
		}
		

}
