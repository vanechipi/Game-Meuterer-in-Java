package dad.GameState;

import dad.Game.Game;
import dad.Game.Role;
import dad.server.IPlayer;

/**
 * 
 * Se actualiza el rol del capitan en caso de haber perdido el motin
 * Se descartan los roles de los demas jugadores
 */

public class EndOfRoundState extends State {

	public EndOfRoundState(Game game) {
		super(game);
		
	}

	@Override
	public void compute() {
		
		IPlayer oldCaptain = game.getPlayerWhithRole("CAPITAN");		;
		IPlayer amotinado = null;
		
		//Si el motin fue sofocado se actualiza el rol del capitan
		if(game.thereRiot()){
			     
			amotinado = game.getPlayerWhithRole("AMOTINADO");	    //obtenemos el jugador con rol amotinado
			Role roleAmotinado = amotinado.getCharacter();			//almacenamos el rol amotinado
			Role roleCaptain = oldCaptain.getCharacter();  	 			//almacenamos el rol capitan
												
			amotinado.setCharacter(roleCaptain); 					//El amotinado pasa a ser el nuevo capitan
			amotinado.setTurn(1);									//El antiguo amotinado pasa a tener el turno 1
			oldCaptain.setCharacter(null);								//Quitamos el rol al antiguo capitan
			oldCaptain.setTurn(null);									//Se actualiza el turno del nuevo capitan
			game.setPositionCaptain(game.getSortedPlayers().indexOf(amotinado));			//Se actualiza la position del capitan en la lista de jugadores.
			game.getDeck().getRoleCards().add(roleAmotinado);   //Se a�ade rol AMOTINADO a la baraja para siguiente ronda
		}else{
			game.setPositionCaptain(game.getSortedPlayers().indexOf(oldCaptain));
		}
		//Quitamos roles a los personajes menos al capitan y los a�adimos a la baraja.
		for(IPlayer player:game.getSortedPlayers()){
			
			if(player.getCharacter()!=null){ // Si el jugador tiene algun rol 
				if(!player.getCharacter().getName().equals("CAPITAN")){  //Si el jugador No es el capitan
					game.getDeck().getRoleCards().add(player.getCharacter());   //Se a�ade el rol a la baraja para siguiente ronda
					player.setCharacter(null);	//Quitamos el rol al jugador
				}
			}
		}
		game.setBoatswainPoints(-1); // se resetea el salario del contramaestre
		game.setThereRiot(false);
		game.setRound(game.getRound()+1); //Sumamos una ronda mas
		game.currentState = new IdleState(this.game);
		
	}

}
