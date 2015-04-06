package dad.Command;

import java.util.ArrayList;
import java.util.List;

import dad.Game.Commodity;
import dad.server.Message;
import dad.server.Player;
import dad.Game.Game;

public class DiscardStevedoreCommand extends GameCommand {
	private String nameCard1;
	private String nameCard2;
	private String nameCard3;
	
	public  DiscardStevedoreCommand(String command, Player cc) {
		super (command,cc);
	}

	@Override
	public String execute()
	{
		throw new UnsupportedOperationException("This is a game command, 'execute' needs a parameter of type Game");
	}

	public String execute(Game game) {
		String res = null;
		String [] parts = command.split(" ");
		
		if (parts.length == 4) {
			if(!player.getIsLobby()) {
				nameCard1 = parts[1];
				nameCard2 = parts[2];
				nameCard3 = parts[3];
				
				
				List<Commodity> hand = new ArrayList<Commodity>();
				hand.addAll(player.getHand());
				Commodity commodity1= player.commodityOfHand(nameCard1);
				if(commodity1 != null ) {
					
					player.getHand().remove(commodity1);			//La emilinamos de la mano
					Commodity commodity2= player.commodityOfHand(nameCard2);
					if(commodity2 != null ) {
						player.getHand().remove(commodity2);			//La emilinamos de la mano
						Commodity commodity3= player.commodityOfHand(nameCard3);
						if(commodity3 != null){
							player.getHand().remove(commodity3);			//La emilinamos de la mano
							game.getDiscard().CommodityCards.add(commodity1);				//metemos en descarte la carta elegida
							game.getDiscard().CommodityCards.add(commodity2);				//metemos en descarte la carta elegida
							game.getDiscard().CommodityCards.add(commodity3);				//metemos en descarte la carta elegida
							player.sendMessage(new Message("Cartas descartadas: "+ commodity1+", "+ commodity2 +", "+ commodity3));
							res = Message.SUCCES_CHOOSE_CARD;
						}else{
							res= Message.ERROR_DONT_EXIT_CARD;
						}
						
					}else{res= Message.ERROR_DONT_EXIT_CARD;}
					
				}else {res= Message.ERROR_DONT_EXIT_CARD;}
				
				if(res == Message.ERROR_DONT_EXIT_CARD){
					//Si hay error volvemos a dejar la mano del jugador como estaba
					// y se eliminan 'aleatoriamente'.
					player.getHand().clear();
					player.getHand().addAll(hand);
					
					commodity1=player.getHand().get(0);
					game.getDiscard().CommodityCards.add(commodity1);				//metemos en descarte la carta elegida
					player.getHand().remove(commodity1);			//La emilinamos de la mano
					
					
					Commodity commodity2=player.getHand().get(1);
					game.getDiscard().CommodityCards.add(commodity2);				//metemos en descarte la carta elegida
					player.getHand().remove(commodity2);			//La emilinamos de la mano
					
					
					Commodity commodity3=player.getHand().get(2);
					game.getDiscard().CommodityCards.add(commodity3);				//metemos en descarte la carta elegida
					player.getHand().remove(commodity3);			//La emilinamos de la mano
					
					
					player.sendMessage(new Message("Hubo un error con las cartas elegidas.\nCartas descartadas aleatoriamente: "+ commodity1+", "+ commodity2 +", "+ commodity3));
					
				}
				
			}else {
				res = Message.ERROR_ISNT_GAME;
			}
		} else {
			res = "Se esperaban 4 parametros";
		}
		return res;
	}
}