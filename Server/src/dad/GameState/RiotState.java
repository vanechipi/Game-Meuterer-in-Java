package dad.GameState;

import dad.Game.Game;
import dad.Game.Room;
import dad.server.Message;
import dad.server.IPlayer;

/****
 * 
 * 
 * Se resuelve el motin
 * 
 *
 */

public class RiotState extends State {
	
	public RiotState(Game game) {
		super(game);
	
	}

	@Override
	public void compute() {
		
		
		IPlayer captain = game.getPlayerWhithRole("CAPITAN");
		IPlayer contramaestre = game.getPlayerWhithRole("CONTRAMAESTRE");
		IPlayer amotinado = game.getPlayerWhithRole("AMOTINADO");
		IPlayer grumete = game.getPlayerWhithRole("GRUMETE");
		
		Integer teamCaptain,teamAmotinado=0;     				
		if(contramaestre!=null){          // El  Contramaestre otorga 1 carta de Conflicto adicional a su bando
			teamCaptain=1;
		}
		//En el metodo numberConclictCardPlayer se comprueba que el jugador con dicho rol exista (!=null)
		teamCaptain = game.numberConflictCardPlayer(captain) + game.numberConflictCardPlayer(contramaestre);
		teamAmotinado = game.numberConflictCardPlayer(amotinado) + game.numberConflictCardPlayer(grumete);
		
		
		if(teamCaptain<=teamAmotinado){        						//Si gana el motin 
			captain = game.getPlayerWhithRole("AMOTINADO");		//El capitan ahora sera el amotinado
			Room.sendMessageToAll(null,new Message("HAN GANADO EL MOTIN! \nEl nuevo capitan es "+ captain.getName()));
			game.setThereRiot(true);
		
		}else{
			Room.sendMessageToAll(null,new Message("Han perdido el motin \nEl  capitan sigue siendo "+ captain.getName()));
			game.setThereRiot(false);
		}
		
		
		game.currentState= new VictoryPointsRecountState(this.game);
	}

}
