package dad.Game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import Exceptions.PlayerLeavingGameException;
import dad.Command.*;
import dad.GameState.IdleState;
import dad.GameState.State;
import dad.server.IPlayer;
import dad.server.LPlayer;
import dad.server.Message;
import dad.server.Player;


public class Game implements Runnable{
	public  State currentState;
	public  Boolean isWait;
	
	private List<IPlayer> sortedPlayers;
	private List<IPlayer> playersWinner;
	private Map<IPlayer,Map<String,List<Commodity>>> chosenCommodityIsland;
	private Integer positionCaptain;
	private Integer captainDestinationCard;
	private Integer mutineerDestinationCard;
	private Boolean thereRiot;
	
	private CardsPack deck;
	private CardsPack discard;

	private Integer activeIsland;
	private Integer previousIsland;
	private Integer round;
	private Integer boatswainPoints; //puntos contramaestre; 
	
	private Timer timer;
	public final static int TIMEOUT = 500000; //tiempo de espera en milisegundos.
	
	public Game(List<IPlayer> players) {
		this.sortedPlayers = players;
		this.chosenCommodityIsland = new HashMap<IPlayer,Map<String,List<Commodity>>>();
		addPlayerToMap(); //aÃ±adimos jugadores al mapa de cartasIslasElegidas
		this.positionCaptain=0;
		this.captainDestinationCard=0;
		this.mutineerDestinationCard=0;
		this.thereRiot=false;
		this.activeIsland=0;
		this.previousIsland=-1;
		this.round=1;
		this.boatswainPoints=-1;
		currentState = new IdleState(this);
		isWait = false;
		timer = new Timer();
		deck = new CardsPack(false);
		discard = new CardsPack(true);
		playersWinner = new ArrayList<IPlayer>();
	}

	// Logica del servidor por estados
	public void run() {
		// si se sale un jugador la partida se termina
		while(getRound() <= 8 && getNumberPlayers() == 4 ) {
			try {
                currentState.compute();
			} catch(PlayerLeavingGameException e) {
				Room.sendMessageToAll(null, new Message("Partida finaliza. El jugador al que le toca turno ya no está."));
				
				break;
			}
		}
		
		// Fin de las 8 rondas
		//Comparamos puntos y sacamos el ganador o ganadores en caso de que haya empate
		
		setPlayerWinners();
		Room.sendMessageToAll(null, new Message(Message.ESPACE));
		Room.sendMessageToAll(null, new Message("FIN DEL JUEGO\nGanadores: "+playersWinner.toString()));
		//Indicamos el comando que pueden realizar
		for(IPlayer player: sortedPlayers){
			if (!(player instanceof LPlayer)){
				player.sendMessage(new Message("-SALIR"));
			}
		}
		
	}

	// Comprueba el comando que ha escrito el jugador y reanuda el hilo segun.
	public void receive(Player player, Command command){
		if(isWait){
			if(!(command instanceof ShowAvailableCommands) && !(command instanceof ErrorCommand)){
				if((command instanceof PassCommand)){	//Si ha escrito PASAR
					player.setPassState(true);		//Activa el PassState del jugador
				}
				if(command instanceof LeaveRoomCommand) {
					// Cambiamos player por leavingplayer
					// leaveAllPlayerFromGame();
					timer.cancel();
					sortedPlayers.set(sortedPlayers.indexOf(player), new LPlayer());
					isWait = false;
					resume();
					//throw new PlayerLeavingGameException();
				} else {
					isWait = false;
					resume();
				}
			}
		}
		
	}

	public String execute(GameCommand command)
	{
		return command.execute(this);
	}

	

	public void timerChooseRol(IPlayer player){
		this.timer = new Timer();
		this.timer.schedule(missedTurnAndRol(player), TIMEOUT);
	}
	
	public TimerTask missedTurnAndRol(final IPlayer p){
		TimerTask t = new TimerTask(){
			public void run(){
				p.sendMessage(new Message(Message.MISSED_TURN));
				if(p.getCharacter()==null) {
					List<Role> cardsRole= deck.getRoleCards();
					Integer sizeList= cardsRole.size();
					int RandomNumber = (int) (Math.random()*sizeList);
					Role newRole=cardsRole.get(RandomNumber);
					p.setCharacter(newRole);
					deck.getRoleCards().remove(newRole);
					p.sendMessage(new Message("Tu rol se eligira aleatoriamente. Tu rol es: "+ newRole));

				}
				Room.sendMessageToAll(p, new Message(p+" ha pasado."));
				p.setPassState(true);
				resume();
			}
		};
		return t;
	}
	public void timerChooseSalary(IPlayer player){
		this.timer = new Timer();
		this.timer.schedule(chooseSalary(player), TIMEOUT);
	}
	public TimerTask chooseSalary(final IPlayer p){
		TimerTask t = new TimerTask(){
			
			public void run(){
				p.sendMessage(new Message(Message.MISSED_TURN));
				int randomNumber = (int) (Math.random()*4);
				p.sendMessage(new Message("El sueldo del contramaestre se eligira aleatoriamente"));
				Room.sendMessageToAll(null,new Message("Sueldo del contramaestre: "+randomNumber));
				setBoatswainPoints(randomNumber);
				resume();
			}
		};
		return t;
	}
	
	public synchronized void pause(IPlayer player) throws InterruptedException {
		if(player.getCharacter() != null && player.getCharacter().getName().equals("CAPITAN") 
				&& boatswainPoints==-1){
			timerChooseSalary(player);
			
		}else { timerChooseRol(player); }
		
		wait();	
		this.timer.cancel();
	}
	
	public synchronized void pauseStevedore(IPlayer player) throws InterruptedException {
		this.timer = new Timer();
		this.timer.schedule(discardStevedore(player), TIMEOUT);
		wait();	
		this.timer.cancel();
	}
	public TimerTask discardStevedore(final IPlayer p){
		TimerTask t = new TimerTask(){
			
			public void run(){
				p.sendMessage(new Message(Message.MISSED_TURN));
				List<Commodity> cards =  p.getHand();
				
				Commodity commodity1=cards.get(0);
				getDiscard().CommodityCards.add(commodity1);	//metemos en descarte la carta elegida
				p.getHand().remove(commodity1);					//La emilinamos de la mano
				
				
				Commodity commodity2=cards.get(1);
				getDiscard().CommodityCards.add(commodity2);	//metemos en descarte la carta elegida
				p.getHand().remove(commodity2);					//La emilinamos de la mano
				
				
				Commodity commodity3=cards.get(2);
				getDiscard().CommodityCards.add(commodity3);	//metemos en descarte la carta elegida
				p.getHand().remove(commodity3);					//La emilinamos de la mano
				
				
				p.sendMessage(new Message("Cartas descartadas aleatoriamente: "+ commodity1+", "+ commodity2 +", "+ commodity3));
				resume();
			}
		};
		return t;
	}

	

	public synchronized void resume() {
		notify();
	}
	
	public void setPlayerWinners(){
		Integer count=0;
		for(IPlayer player: sortedPlayers){
			if (!(player instanceof LPlayer)){
				if(player.getPoints()>count){
					count=player.getPoints();
					playersWinner.clear();
					playersWinner.add(player);
				}else if(player.getPoints()==count){
					playersWinner.add(player);
					
				}
			}
		}
		
	}
	
	public void addPlayerToMap(){
		for(IPlayer player:sortedPlayers){
			Map<String,List<Commodity>> map= new HashMap<String,List<Commodity>>();
			getChosenCardsIslands().put(player,map);
		}
	}
	
	//Vacia del maPa , todas las cartas que se hayan jugado en cada isla.
	public void emptyCommodityToMap(){
		
		for(IPlayer player:sortedPlayers){
			Set<String> islandList = getChosenCardsIslands().get(player).keySet();
			for(String island: islandList){
				getChosenCardsIslands().get(player).get(island).clear();
			}
			
		}
	}

	public  boolean distributeCards(){
		
		int numberCardsPlayer;                 // Numero de cartas que tiene el jugador
		Integer numberCards;                   //Numero de cartas que hay que repartir al jugador 
		 
		
		for(IPlayer player: sortedPlayers){
			
			List<Commodity> listCard= new ArrayList<Commodity>();         // lista de cartas a repartir
			numberCardsPlayer = player.getHand().size();
			numberCards = 5 - numberCardsPlayer;
			if(player.getCharacter()!=null){
				if(player.getCharacter().getName().equals("ESTIBADOR")){
					numberCards +=3;                   // Si es el estibador se le repartes 3 cartas mas y luego elegirï¿½ 3 que no quiera.
				}
			}
			
			if(numberCards!=0){
				
				listCard=deck.getCommodityCards(numberCards);
				
				if(discard.getCommodityCards().size()!=0){
					if(listCard.size()<numberCards){                     // Si no habia suficiente cartas hay que cogerlas del la baraja Descartes
						numberCards = numberCards - listCard.size() ;
						deck.getCommodityCards().addAll(discard.getCommodityCards());  //Se vuelca Descartes en Bajara
						discard.getCommodityCards().clear();							//Se vacia Descartes.
						listCard.addAll(deck.getCommodityCards(numberCards));
					}
					
				}
				player.setHand(listCard);                            //Isertamos las cartas a la mano del jugador.
				deck.getCommodityCards().removeAll(listCard);		//quitamos de la baraja las cartas ya elegidas para cada jugador.
			}
				
		}
		
		return true;
	}

	//Cambia estado de pass
	public void resetStatePlayer(){
		for(IPlayer player: sortedPlayers){
			player.setPassState(false);
		}
	}
	
	//Devuelve el jugador con un Rol determinado.
	public IPlayer getPlayerWhithRole(String role){
		IPlayer playerRole=null;
		for(IPlayer player: sortedPlayers){
			if(player.getCharacter()!=null)
				if(player.getCharacter().getName().equals(role)){
					playerRole=player;
				}
		}
		return playerRole;
	}
	
	//Devuleve true si todos los jugadores tienen un rol es decir si han pasado
	public Boolean allPlayerPass(){
		Boolean result=true;
		for(IPlayer player:sortedPlayers){
			if(player.isPassState()==false || player.getCharacter()==null){
				result=false;
				break;
			}
		}
		return result;
	}
	
	//Numero de cartas conflicto que tiene un jugador
	public  Integer numberConflictCardPlayer(IPlayer player){
		Integer numberConclict=0;
		
		if(player!=null){
		
			List<Commodity> copyHand = new ArrayList<Commodity>(player.getHand());
		
			for(Commodity card: copyHand){
				if(card.getName().equals("CONFLICTO")){
					numberConclict++;
					player.getHand().remove(card);
					discard.CommodityCards.add(card);
				}
					
			}
		}
		return numberConclict;
		
	}
	
	//Muestra el rol de todos los jugadores
	public void showAllCharacter(){
		for(IPlayer player:sortedPlayers){
			Room.sendMessageToAll(null,new Message("> "+player.getName()+" es el "+player.getCharacter()));
		}
	}
	
	/* updateRoomPlayer()
	 * Actualiza la room del player, es decir, hay que indicar en que Room estïa dicho jugador e inserta a la lista de Room
	 en el servidor.Esto es modificando el atributo Room que hay en la clase Player y Server*/

	//Numero de jugadores 
	public Integer getNumberPlayers() {
		return sortedPlayers.size();
	}
	
	public Map<IPlayer,Map<String,List<Commodity>>> getChosenCardsIslands(){
		return chosenCommodityIsland;
	}
	
	public Island getActiveIsland() {
		Island island = getDeck().getIslandCards().get(activeIsland);
		return island;
	}
	public Integer getPositionActiveIsland() {
		return activeIsland;
	}

	public void setPositionActiveIsland(Integer activeIsland) {
		this.activeIsland = activeIsland;
	}

	public Island getPreviousIsland() {
		Island island = getDeck().getIslandCards().get(previousIsland);
		return island;
	}
	public Integer getPositionPreviousIsland() {
		return previousIsland;
	}

	public void setPositionPreviousIsland(Integer previousIsland) {
		this.previousIsland = previousIsland;
	}

	public Integer getBoatswainPoints() {
		return boatswainPoints;
	}

	public void setPositionCaptain(Integer position) {
		this.positionCaptain=position;
	}

	public Integer getPositionCaptain() {
		return positionCaptain;
	}

	public void setBoatswainPoints(Integer boatswainPoints) {
		this.boatswainPoints = boatswainPoints;
	}
	
	public List<IPlayer> getSortedPlayers() {
		return sortedPlayers;
	}

	public Integer getPositionCaptainDestinationCard() {
		return captainDestinationCard;
	}

	public void setCaptainDestinationCard(Integer cartaDestinoCapitan) {
		this.captainDestinationCard = cartaDestinoCapitan;
	}

	public Integer getPositionMutineerDestinationCard() {
		return mutineerDestinationCard;
	}
	

	public void setPositionMutineerDestinationCard(Integer mutineerDestinationCard) {
		this.mutineerDestinationCard = mutineerDestinationCard;
	}

	public Boolean thereRiot(){
		return thereRiot;
	}

	public void setThereRiot(Boolean thereRiot){
		this.thereRiot=thereRiot;
	}

	public Integer getRound() {
		return round;
	}

	public void setRound(Integer round) {
		this.round = round;
	}

	public List<IPlayer> getPlayerWinner(){
		return playersWinner;
	}

	
	
	public  CardsPack getDiscard(){
		return discard;
	}
	
	public  CardsPack getDeck(){
		return deck;
	}
}
