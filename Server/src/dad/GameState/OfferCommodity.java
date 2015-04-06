package dad.GameState;



import dad.Game.Game;
import dad.Game.Island;
import dad.Game.Room;
import dad.server.Message;
import dad.server.IPlayer;

/****
Turnos de cada jugador.
Se realiza la ronda hasta que todos los jugadores tenga un rol asignado

****/
public class OfferCommodity extends State {
	
	public OfferCommodity(Game game) {
		super(game);
	}

	@Override
	public void compute() {
		while(!game.allPlayerPass()) {
			//Recorremos la lista de los jugadores ordenados por turno
			for(IPlayer sPlayer: game.getSortedPlayers()) {
				Integer sPlayerPosition = game.getSortedPlayers().indexOf(sPlayer);
				Room.sendMessageToAll(sPlayer,new Message("Turno de "+sPlayer.getName()));
				sPlayer.sendMessage(new Message(Message.IS_YOUR_TURN));
				
				
				sPlayer.sendMessage(new Message("Ronda: "+game.getRound()+",\nEl barco esta en "+ game.getActiveIsland()));
				if(game.getPositionPreviousIsland() >= 0 && game.getPositionActiveIsland() != game.getPositionPreviousIsland()){   //Comprobamos que el barco venga de alguna isla	 
					sPlayer.sendMessage(new Message("El barco viene de "+ game.getPreviousIsland()));		
				}
				
				sPlayer.sendMessage(new Message("Cartas Jugadas: \b"+game.getChosenCardsIslands()));
				
				//Se actualizan comandos permitidos
				sPlayer.getCommandEnabled().clear();
				sPlayer.getCommandEnabled().add("PASAR");
				sPlayer.getCommandEnabled().add("SALIR");
				if(!sPlayer.isPassState()) {
					//Se actualizan comandos permitidos
					sPlayer.getCommandEnabled().add("ELEGIR_CARTA");
					sPlayer.sendMessage(new Message("Tus cartas son: "+sPlayer.getHand().toString()));
					sPlayer.sendMessage(new Message("-ELEGIR_CARTA ISLA CARTA"+"\n-PASAR"));
				}else {
					sPlayer.sendMessage(new Message("-PASAR"));
				}

				game.isWait = true;
				try {
					game.pause(sPlayer);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				sPlayer = game.getSortedPlayers().get(sPlayerPosition);
				sPlayer.getCommandEnabled().clear();
				sPlayer.getCommandEnabled().add("SALIR");
				
				if(sPlayer.isPassState()){ //Si el jugador ha elegido pasar
					if(sPlayer.getCharacter()==null){                         //Si ha elegido PASAR y no tiene ningun rol
						sPlayer.getCommandEnabled().add("ELEGIR_ROL");
						sPlayer.sendMessage(new Message("Elige un rol entre los disponibles: "+ game.getDeck().getRoleCards()));
						sPlayer.sendMessage(new Message("-ELEGIR_ROL NOMBRE"));
						game.isWait = true;
						try {
							game.pause(sPlayer);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						sPlayer.getCommandEnabled().clear();
						sPlayer.getCommandEnabled().add("SALIR");
					}else{
						if(sPlayer.getCharacter().getName().equals("CAPITAN")) {					//Si ha elegido PARAR  el capitan
							Integer numberCard= sPlayer.getHand().size();						//Se obtiene el numero de cartas que le quedan
							game.setCaptainDestinationCard(numberCard + game.getPositionActiveIsland());   					//La isla siguiente ser� tantas posiciones como cartas tenga
							if(game.getPositionCaptainDestinationCard()>=12) {											//Si se pasa de la carta 12
								game.setCaptainDestinationCard(game.getPositionCaptainDestinationCard() - 12);  				//Vuelve al principio 
							}
							Room.sendMessageToAll(null,new Message("Carta destino del capitan "+game.getDeck().getIslandCards().get(game.getPositionCaptainDestinationCard())));
						}
					}
				}
			} //Fin del for.
		}	//Fin de la ronda
		game.currentState= new ShowRoles(this.game);
	}
}
