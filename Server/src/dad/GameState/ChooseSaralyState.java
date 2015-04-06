package dad.GameState;

import Exceptions.PlayerLeavingGameException;
import dad.Game.Game;
import dad.Game.Room;
import dad.server.IPlayer;
import dad.server.Message;

/***
 * 
El capitan elige un sueldo al contramestre

***/

public class ChooseSaralyState extends State {

	public ChooseSaralyState(Game game) {
		super(game);
		
	}

	public void compute() {
		//Se actualiza lista de comandos permitidos
		IPlayer captain = game.getSortedPlayers().get(0);
		
		if(captain != null){ 		//Si hay capitan	
			captain.getCommandEnabled().clear();	//Se limpia la lista de comandos permitidos
			captain.getCommandEnabled().add("SUELDO_CONTRAMAESTRE");	//Añadimos el comando SUELDOCONTRAMAESTRE
			captain.getCommandEnabled().add("SALIR");					//Añadimos el comando SALIR
			captain.sendMessage(new Message(Message.CHOOSE_POINTS));	//Se le manda el mensaje de que elija puntos
			captain.sendMessage(new Message("-SUELDO_CONTRAMAESTRE numero"));
		
			Room.sendMessageToAll(captain, new Message(Message.WAIT));
			game.isWait = true; 		//Activamos el estado en espera a que el jugador escriba
			
			try {
				game.pause(captain);		//Pausamos el juego 
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			captain.getCommandEnabled().clear();		
			game.currentState = new OfferCommodity(this.game); 
		}
	}
}
