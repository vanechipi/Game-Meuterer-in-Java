package dad.GameState;

import java.util.ArrayList;
import java.util.List;

import dad.Game.Commodity;
import dad.Game.Game;
import dad.Game.Room;
import dad.server.Message;
import dad.server.IPlayer;


/******
 * 
 * Recuento de puentos de vicotira 
 * teniendo en cuenta el motin y las mercancias vendidas
 *
 */

public class VictoryPointsRecountState extends State {

	public VictoryPointsRecountState(Game game) {
		super(game);
	}
	
	@Override
	public void compute() {
		
		Integer victoryPoints;
		Integer pointsCommodityActiveIsland = 0; //puntos de victoria de la isla actual
		Integer pointsCommodityPreviousIsland = 0;	//Puntos de victoria de la isla anterior
		
		List<IPlayer> playersWinnerActiveIsland = new ArrayList<IPlayer>();	 //Lista de ganadores de la isla actual
		List<IPlayer> playersWinnerPreviousIsland = new ArrayList<IPlayer>(); //lista de ganadores de la isla anterior
		
		String activeIsland = game.getActiveIsland().getName().toUpperCase(); //Almacenamos el nombre de la isla actual
		String previousIsland = null;
		
		for(IPlayer player: game.getSortedPlayers()){  //Recorremos los jugadores ordenados por turnos
			victoryPoints = 0;
			List<Commodity> listCommodityActiveIsland = game.getChosenCardsIslands().get(player).get(activeIsland);	//Lista de mercancias vendidas por el jugador en la isla actual
			
			if(listCommodityActiveIsland != null){ //Si el jugador ha vendido alguna mercancia 
				if(pointsCommodityActiveIsland < listCommodityActiveIsland.size()){					//Si es el que mas puntos tiene por ahora
					pointsCommodityActiveIsland = listCommodityActiveIsland.size();					//Actualizamos Puntos de Mercancia de ActiveIsland
					playersWinnerActiveIsland.clear();											//Eliminamos los jugadores que hay en la lista de ganadores
					playersWinnerActiveIsland.add(player);										//Lo anadimos a la lista de ganadores que hay por ahora
				}else if(pointsCommodityActiveIsland == listCommodityActiveIsland.size()){				//Si empata en los puntos 
						playersWinnerActiveIsland.add(player);									//Lo anadimos a la lista de ganadores que hay por ahora
					}
				
			}
			
			if(game.getPositionPreviousIsland() >=0 && game.getPositionPreviousIsland() != game.getPositionActiveIsland()){						//Si hay previusIsland
				previousIsland= game.getDeck().getIslandCards().get(game.getPositionPreviousIsland()).getName().toUpperCase();
				//Se hace lo mismo que con la Active
				List<Commodity> listCommodityPreviusIsland = game.getChosenCardsIslands().get(player).get(previousIsland);
				
				if(listCommodityPreviusIsland != null)
					if(pointsCommodityPreviousIsland < listCommodityPreviusIsland.size()){			//Si es el que mas puntos tiene por ahora
						
						pointsCommodityPreviousIsland = listCommodityPreviusIsland.size();			//Actualizamos Puntos de Mercancï¿½a de ActiveIsland
						playersWinnerPreviousIsland.clear();		//Eliminamos los jugadores que hay en la lista de ganadores
						playersWinnerPreviousIsland.add(player);								//Lo aï¿½adimos a la lista de ganadores que hay por ahora
					}else
					
						if(pointsCommodityPreviousIsland == listCommodityPreviusIsland.size()){			//Si empata en los puntos 
							
							playersWinnerPreviousIsland.add(player);								//Lo insertamos en la lista de ganadores que hay por ahora
						}
					
			}
			
			
			if(game.thereRiot()){
				
				if(player.getCharacter().getName().equals("AMOTINADO")){	//Si es el amotinado
					victoryPoints = game.getDeck().getIslandCards().get(game.getPositionMutineerDestinationCard()).getVps(); //Puntos de navegacion de la isla destino
					player.addPoints(victoryPoints);
				}
				if(player.getCharacter().getName().equals("GRUMETE")){ //Si es el grumete
					victoryPoints = 2;		
					player.addPoints(victoryPoints);//gana 2 puntos extra por ganar el motin
				}
				
			}else{
				
				if(player.getCharacter().getName().equals("CAPITAN")){ //Si es el capitan
					victoryPoints = game.getDeck().getIslandCards().get(game.getPositionCaptainDestinationCard()).getVps(); //puntos de navegacion de la isla destino
					if(game.getPlayerWhithRole("CONTRAMAESTRE")!=null)
						victoryPoints -= game.getBoatswainPoints();	//Se resta el salario que le ofrecio al contramaestre
					player.addPoints(victoryPoints);
				}
				if(player.getCharacter().getName().equals("CONTRAMAESTRE")){ //Si es el contramaestre
					victoryPoints = 1 + game.getBoatswainPoints();			//gana 1 punto extra mas el sueldo que le da el capitan
					player.addPoints(victoryPoints);
				}
			}
			
			
		}// fin for
			
		// Segun el numero de ganadores asignamos puntos
		IPlayer mercader = game.getPlayerWhithRole("MERCADER");
		victoryPoints=0;
		
		if(playersWinnerActiveIsland.size() == 1 ){ //Si solo hay un ganador
			victoryPoints = game.getActiveIsland().getWinnerPoint();
		}
		if(playersWinnerActiveIsland.size() == 2 ){ //Si hay empate a 2
			victoryPoints = game.getActiveIsland().getTwoTiePoint();
		}
		if(playersWinnerActiveIsland.size() == 3 ){ //Si hay empate a 3
			victoryPoints =game.getActiveIsland().getThreeTiePoint();
		}
		
		for(IPlayer pWinner: playersWinnerActiveIsland){ //Recorremos la ista de ganadores de la isla activa
			if(pWinner.equals(mercader)){						//El Mercader se lleva los puntos como si no hubiera empatado
				pWinner.addPoints(game.getActiveIsland().getWinnerPoint());
			}else{
				pWinner.addPoints(victoryPoints);					//Actualizamos puntos de los ganadores
			}
		}
		Room.sendMessageToAll(null,new Message("Ganadores "+ game.getActiveIsland().getName()+"-->"+playersWinnerActiveIsland.toString()));
		
		
		if(game.getPositionPreviousIsland() >=0 && game.getPositionPreviousIsland() != game.getPositionActiveIsland()){ 					//Si la isla activa es distinta a la anterior.
			victoryPoints=0;
			// Se hace lo mismo con previousIsland
			if(playersWinnerPreviousIsland.size() == 1 ){
				victoryPoints = game.getPreviousIsland().getWinnerPoint();
			}
			if(playersWinnerPreviousIsland.size() == 2 ){
				victoryPoints = game.getPreviousIsland().getTwoTiePoint();
			}
			if(playersWinnerPreviousIsland.size() == 3 ){
				victoryPoints = game.getPreviousIsland().getThreeTiePoint();
			}
			
			for(IPlayer pWinner: playersWinnerPreviousIsland){
				if(pWinner.equals(mercader)){					//El Mercader se lleva los puntos como si no hubiera empatado
					pWinner.addPoints(game.getPreviousIsland().getWinnerPoint());
				}else
					pWinner.addPoints(victoryPoints);				//Actualizamos puntos de los ganadores
			}
			Room.sendMessageToAll(null,new Message("Ganadores "+game.getPreviousIsland().getName()+"-->"+playersWinnerPreviousIsland.toString()));
			
		
		}
		
		for(IPlayer player: game.getSortedPlayers()){
			Room.sendMessageToAll(null,new Message("Puntos de victoria totales jugador "+player.getName()+"--->"+player.getPoints()));
		}
		if(game.getRound()<8){
			game.currentState=new CardDrawing(this.game);
		}else{ //Aqui se suma una ronda para que en el caso de que esté en la ronda 8 se quede con 9 y salga del while principal. Sin necesidad de ir al Carddrawing
			game.setRound(game.getRound()+1);
		}
	}
}
