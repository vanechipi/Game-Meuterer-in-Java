package dad.GameState;

import dad.Game.Game;
import dad.Game.Room;
import dad.server.Message;
import dad.server.IPlayer;

/***
 * 
 * Actualizamos la isla destino dependiendo de si el motin fue sofocado o no
 * Repartimos cartas a los jugadores hasta tener 5 (8 el estibador).
 * El estibador tendra que descartarse de 3 
 *
 */

public class CardDrawing extends State {

	public CardDrawing(Game game) {
		super(game);
	}

	@Override
	public void compute() {
		
		game.emptyCommodityToMap();			//Vaciamos mapa del jugador
		game.setPositionPreviousIsland(game.getPositionActiveIsland()); 		//Se Actualiza la isla Anterior (previousIsland)
		
		//Activamos islas dependiendo de si hay motin
		if(game.thereRiot()){
			game.setPositionActiveIsland(game.getPositionMutineerDestinationCard());
		}else{
			game.setPositionActiveIsland(game.getPositionCaptainDestinationCard());
		}
		
		game.distributeCards();			//Repartimos cartas
		
		//El estibador tiene que descartarse de 3 cartas
		IPlayer stevedore = game.getPlayerWhithRole("ESTIBADOR");
		if(stevedore != null){ //si hay estibador 
			//Actualizamos comandos permitidos
			stevedore.getCommandEnabled().clear();
			stevedore.getCommandEnabled().add("SALIR");
			stevedore.getCommandEnabled().add("DESCARTAR");
			
			Room.sendMessageToAll(stevedore, new Message("Esperando a que el ESTIBADOR se descarte"));
			stevedore.sendMessage(new Message("Por tu habilidad especial tienes 8 cartas, debes descartarte de las 3 que quieras.\n-DESCARTAR carta1 carta2 carta3"));
			stevedore.sendMessage(new Message(stevedore.getHand().toString()));
			game.isWait = true;  //El juego pasa a estado de espera
			try {
				game.pauseStevedore(stevedore);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			stevedore.getCommandEnabled().clear();  
		}
		
		game.currentState = new EndOfRoundState(this.game);
		
	}

}
