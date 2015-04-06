package dad.Game;

public class Commodity extends Card {
	private CardType.Commodity type;
	private String direction;
	
	public Commodity(String name,CardType.Commodity typeCommodity,
			String direction){
		super(name,CardType.Base.MERCANCIA);
		this.type=typeCommodity;
		this.direction=direction;
	}

	public CardType.Commodity getSubType() {
		return type;
	}
	
	public String getDirection(){
		return this.direction;
	}

	public void setSubType(CardType.Commodity type) {
		this.type = type;
	}

	public String toString(){
		return this.getName();
	}
}