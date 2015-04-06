package dad.GameState;

import dad.Game.Game;
import dad.Game.Room;
import dad.server.Message;
import dad.server.IPlayer;

/*******
 * 
 * Muestra los Personajes de los juegadores
 * 
 *
 */


public class ShowRoles extends State {
	
	public ShowRoles(Game game) {
		super(game);
		
	}

	@Override
	public void compute() {
	
		Room.sendMessageToAll(null,new Message(Message.ESPACE));
		//game.getMatch().sendMessageToAll(null,new Message("FIN DE LA RONDA"));
		Room.sendMessageToAll(null,new Message("\nPERSONAJES"));
		game.showAllCharacter(); 			// Revelamos personajes
		
		if(game.getPlayerWhithRole("AMOTINADO")!=null){  //Comprueba que haya Amotinado
			
			IPlayer amotinado =game.getPlayerWhithRole("AMOTINADO");  //Almacenamos el jugadores que tiene el rol de Amotinado
			Integer numberCard= amotinado.getHand().size();			//Almacenamos el numero de cartas que le quedan en mano
			game.setPositionMutineerDestinationCard( numberCard + game.getPositionActiveIsland());	//Calcula la Isla destino 
			
			if(game.getPositionMutineerDestinationCard()>=12){												//Si se pasa de la carta 12
				game.setPositionMutineerDestinationCard(game.getPositionMutineerDestinationCard() - 12);  			//Vuelve al principio 
			}
			Room.sendMessageToAll(null,new Message("Carta destino del AMOTINADO: "+game.getDeck().getIslandCards().get(game.getPositionMutineerDestinationCard())));
			Room.sendMessageToAll(null,new Message("Carta destino del CAPITAN: "+game.getDeck().getIslandCards().get(game.getPositionCaptainDestinationCard())));
			
			game.currentState= new RiotState(this.game);  //Hay que resolver el motin
		
		}else{
			game.currentState= new VictoryPointsRecountState(this.game); //Pasamos a hacer el recuento de puntos de victoria
		}
	}
		 

	
}
