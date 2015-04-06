package dad.Game;

public class Island extends Card {
	private CardType.Commodity typeCommodity;
	private Integer vps;
	private Boolean active;
	private Integer winner;
	private Integer twoTie;
	private Integer threeTie;
	
	public Island(String name, CardType.Commodity typeCommodity,Integer winner, Integer twoTie, Integer threeTie, Integer vps){
		super(name, CardType.Base.ISLA);
		this.winner=winner;
		this.twoTie=twoTie;
		this.threeTie=threeTie;
		this.vps=vps;
		this.active= false;
		this.typeCommodity = typeCommodity;
	}
	
	public CardType.Commodity getTypeCommodity() {
		return typeCommodity;
	}
	public Integer getWinnerPoint(){
		return winner;
	}
	public Integer getTwoTiePoint(){
		return twoTie;
	}
	public Integer getThreeTiePoint(){
		return threeTie;
	}

	public Integer getVps(){
		return vps;
	}
	public Boolean isActive(){
		return active;
	}
	public void setActive(Boolean active ){
		this.active=active;
	}
	public String toString(){
		return "Isla: " + getName()+", "+getTypeCommodity()+", {I:"+getWinnerPoint()+", II:"+getTwoTiePoint()+",III:"+getThreeTiePoint()+"},Puntos Navegacion:"+getVps() ;
	}
}
