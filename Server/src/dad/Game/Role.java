package dad.Game;

public class Role extends Card{
	
	
	public CardType.Role type;
	public CardType.Positioning positioning;

	public Role(String name,CardType.Role type,CardType.Positioning pos){
		
		super(name,CardType.Base.ROL);
		this.type=type;
		this.positioning=pos;
		
	}
	
	public String toString(){
		return type.toString(); 
	}
	public CardType.Role Type(){
		return type;
	}
	

}