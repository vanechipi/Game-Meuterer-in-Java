package dad.GameState;
import dad.Game.Game;
import dad.Game.Island;
import dad.Game.Room;
import dad.server.Message;


/************************
Muestra informacion:
  -En que isla esta el barco
  -De que isla viene el barco
  -Quien es el capitan

*************************/

public class StartState extends State {
	public StartState(Game game) {
		super(game);
	}

	@Override
	public void compute() {
		
		Island island = game.getActiveIsland();	//Almacenamos isla astiva
		
		Room.sendMessageToAll(null,new Message(Message.ESPACE));
		Room.sendMessageToAll(null,new Message("Ronda: "+game.getRound()+",\nEl barco esta en "+island));
		
		if(game.getPositionPreviousIsland()>=0 && game.getPositionActiveIsland()!=game.getPositionPreviousIsland()){   //Comprobamos que el barco venga de alguna isla 
			Room.sendMessageToAll(null,new Message("El barco viene de "+game.getPreviousIsland()));		
		}
		
		Room.sendMessageToAll(null,new Message("El capitan es "+game.getSortedPlayers().get(0).getName()));
		Room.sendMessageToAll(null,new Message(Message.ESPACE));
		
		//Si estamos en la primera ronda repartimos cartas
		if(game.getRound()==1){
			game.distributeCards();                   // Repartimos cartas
		}
		
		game.resetStatePlayer();  					//Reseteamos estados PassState de todos los jugadores
		game.currentState= new ChooseSaralyState(this.game);
	}
}
