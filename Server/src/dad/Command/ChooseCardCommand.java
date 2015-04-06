package dad.Command;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dad.Game.Card;
import dad.Game.Commodity;
import dad.Game.Game;
import dad.Game.Island;
import dad.server.Message;
import dad.server.Player;

public class ChooseCardCommand extends GameCommand {
	private String chosenCard;
	private String chosenIsland;
	
	public  ChooseCardCommand(String command, Player cc) {
		super (command,cc);
		
	}

	@Override
	public String execute()
	{
		throw new UnsupportedOperationException("This is a game command, 'execute' needs a parameter of type Game");
	}

	@Override
	public String execute(Game game) {
		String message = null;
		String [] parts = command.split(" ");
		
			if(parts.length == 3) {
				chosenIsland = parts[1].toUpperCase();
				chosenCard =parts[2];	
				
				if(!player.getIsLobby()) { //Si NO estÃ¡ en el lobby
					
					Commodity card = player.commodityOfHand(chosenCard);
					Island island = game.getDeck().getIsland(chosenIsland);
					
					if(island != null && card != null) { // si la isla y la carta existen
						
						if(game.getActiveIsland().equals(island) ||(game.getPositionPreviousIsland()>=0 && game.getPreviousIsland().equals(island))) {  //Si la isla está activa
							Map<String,List<Commodity>> islandCards = game.getChosenCardsIslands().get(player);//coge el map que guarda este cliente
							if(islandCards.containsKey(chosenIsland)) {
								if(island.getTypeCommodity() != null) {
									if(island.getTypeCommodity().equals(card.getSubType())){//si contiene el mapa la isla activa y ademas la carta escogida es igual a la isla activa
										islandCards.get(chosenIsland).add(card);  											//Se aï¿½ade la carta a la lista de cartas de la Isla
									}
									message=Message.SUCCES_CHOOSE_CARD;
								}else {
									if(islandCards.get(chosenIsland).isEmpty() || islandCards.get(chosenIsland).get(0).getName().equals(card.getName())){	 //Si la mercancia que vendemos es del mismo tipo que el elegido anteriormente
											islandCards.get(chosenIsland).add(card);
											message=Message.SUCCES_CHOOSE_CARD;
									}else{
										message = Message.SUCCES_CHOOSE_CARD ; 
										player.sendMessage(new Message("La carta tiene que ser de tipo: "+islandCards.get(chosenIsland).get(0).getName()));
										
									}
								}
							}else {
								List<Commodity> commodity= new ArrayList<Commodity>();							//si no contiene esta isla , hace una lista nueva con la nueva carta
								if(island.getTypeCommodity() == null || island.getTypeCommodity().equals(card.getSubType())) {
									commodity.add(card);
									islandCards.put(chosenIsland,commodity);										// y asignamos a la isla esta lista
								}
								message=Message.SUCCES_CHOOSE_CARD;	
							}
							if(message==Message.SUCCES_CHOOSE_CARD) {
								game.getDiscard().CommodityCards.add(card);				//metemos en la baraja llamada descarte la carta elegida
								player.getHand().remove(card); 											//quitamos la carta de la mano del jugador
								player.getRoom().sendMessageToAll(player,new Message(player.getName()+" ha tirado "+chosenCard));
							}
						}else{
							message=Message.ERROR_DONT_ACTIVE_ISLAND; //La isla no esta activa
						}
					}else {
						message=Message.ERROR_DONT_EXIT_CARD;
					}
				}else {
					message=Message.ERROR_LEAVE_ISNTROOM;
				}
			}else{
				message = "Se esperaban 3 parametros";
			}
			
			
		return message;
	}
}