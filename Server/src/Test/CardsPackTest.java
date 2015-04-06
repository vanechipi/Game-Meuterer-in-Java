package Test;

import static org.junit.Assert.*;

import org.junit.Test;

import dad.Game.CardsPack;

public class CardsPackTest {

	@Test
	public void createDeckTest() {
		String msg = "Failure executing 'createDeckTest' method ";
		Boolean condition = false;
		
		CardsPack mazo=new CardsPack(false);

		condition=mazo.getIslandCards().size()==12 && mazo.getCommodityCards().size()==36 
				&& mazo.getRoleCards().size()==6 && mazo.getCommodityCards(5).size()==5;
		
		assertTrue(msg, condition);
	}
	
	
	

}
