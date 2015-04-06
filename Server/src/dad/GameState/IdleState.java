package dad.GameState;

import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import dad.Game.Game;
import dad.Game.Role;
import dad.Game.Room;
import dad.server.Message;
import dad.server.IPlayer;


/********************************************************

Si no hay capitan, se elige uno aleatoriamente.
Se asignan los turnos empezando por la derecha del capitan.
El capitan siempre es el primero en tirar 

**********************************************/

public class IdleState extends State {
	

	public IdleState(Game game) {
		super(game);
	}

	@Override
	
	public void compute() {
		
		//Elegimos capitan si no hay
		if(game.getPlayerWhithRole("CAPITAN")==null){						//Si no hay capitan elijo capitan aleatoriamente 
			
			Room.sendMessageToAll(null,new Message("EMPIEZA EL JUEGO"));
			Random rand= new Random();
			game.setPositionCaptain(rand.nextInt(4));	 					// elegimos jugador aleatoriamente 
			Role captainRole = game.getDeck().getRole("CAPITAN"); 	//Guardamos el Rol "Capitan" en una variable 
			game.getSortedPlayers().get(game.getPositionCaptain()).setCharacter(captainRole); 		//Asignamos el rol de capitan al jugador elegido 
			game.getSortedPlayers().get(game.getPositionCaptain()).setTurn(1);  						// El capitan tiene el turno 1
			game.getDeck().RoleCards.remove(captainRole);											// Quitamos el carta capitan de la baraja
						
		}
		
		//Asignamos turnos
		int turn=2;	  														//Los turnos comienza a partir del 2, ya que el 1 lo tiene el capitan
		int posCaptain = game.getPositionCaptain();    						//Almacenamos position del capitan
		for(int i=posCaptain+1;i<4;i++){										//Recorremos los jugadores empezando por la derecha del capitan
			game.getSortedPlayers().get(i).setTurn(turn);					//Asignamos turno
			game.getSortedPlayers().get(i).sendMessage(new Message("Tu turno de juego es "+turn));
			turn++; 														// Aumentamos turno
		}
		
		for(int i=0;i<posCaptain;i++){										//Recorremos los jugadores que faltan desde el inicio hasta el capitan   
			game.getSortedPlayers().get(i).setTurn(turn);					// Asignamos turno
			game.getSortedPlayers().get(i).sendMessage(new Message("Tu turno de juego es "+turn));
			turn++;															//Aumentamos turno
		}	
		
		
		Collections.sort(game.getSortedPlayers(),new Comparator<IPlayer>() {	//Se ordenan jugadores segun el turno
			public int compare(IPlayer p1,IPlayer p2){
				return p1.getTurn().compareTo(p2.getTurn());
			}});
		
		
		game.currentState = new StartState(game);  							//Actualizamos el siguiente estado
	}
}
