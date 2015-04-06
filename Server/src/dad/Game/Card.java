package dad.Game;

public class Card {
	private  CardType.Base type;
	private String name;
	
	public Card(String name,CardType.Base type){
		this.type=type;
		this.name=name;
	}
	
	public CardType.Base getType() {
		return type;
	}
	
	public String getName(){
		return name;
	}

	public void setType(CardType.Base type) {
		this.type = type;
	}
}
