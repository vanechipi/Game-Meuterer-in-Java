package dad.Game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CardsPack {
	public List<Commodity> CommodityCards;
	public List<Island> IslandCards;
	public List<Role> RoleCards;
	
	public CardsPack(boolean empty){
		CommodityCards=new ArrayList<Commodity>();
		IslandCards=new ArrayList<Island>();
		RoleCards=new ArrayList<Role>();
		
		if (!empty){
			this.CreateCards();
			this.shuffle();
		}
	}
	
	public void CreateCards(){
		//Creo las cartas isla
		
		IslandCards.add(new Island("CABO_MONTANOSO",null,4,3,2,5));
		IslandCards.add(new Island("CAVERNA_HELADA",CardType.Commodity.TELAS,3,2,1,3));
		IslandCards.add(new Island("COSTA_VERDE",CardType.Commodity.SAL,5,3,2,4));
		IslandCards.add(new Island("LENGUA_CHICA",CardType.Commodity.TRIGO,6,3,2,4));
		IslandCards.add(new Island("ROCA_HELADA",CardType.Commodity.RUBI,6,4,3,5));
		IslandCards.add(new Island("ISLA_DEL_MONO",CardType.Commodity.TRIGO,2,1,1,2));
		IslandCards.add(new Island("DESPENAFRIOS",CardType.Commodity.TELAS,5,2,2,4));
		IslandCards.add(new Island("COSTA_DEL_SOL",CardType.Commodity.SAL,3,1,1,2));		
		IslandCards.add(new Island("ARRECIBE_ROJO",CardType.Commodity.VINO,4,2,2,3));
		IslandCards.add(new Island("NIDO_DE_PIRATAS",null,4,3,2,5));
		IslandCards.add(new Island("DEDAL",CardType.Commodity.RUBI,2,1,0,2));		
		IslandCards.add(new Island("CABO_ARENOSO",CardType.Commodity.VINO,4,2,1,3));
		
		
		
		//Creo las cartas de Mercancia
		
		for (int i=1;i<=4;i++){
			CommodityCards.add(new Commodity("RUBI",CardType.Commodity.RUBI,"DERECHA"));
		}
		
		for (int i=1;i<=5;i++){
			CommodityCards.add(new Commodity("SAL",CardType.Commodity.SAL,"DERECHA"));
		}
		
		for (int i=1;i<=6;i++){
			CommodityCards.add(new Commodity("VINO",CardType.Commodity.VINO,"DERECHA"));
		}
		
		for (int i=1;i<=7;i++){
			CommodityCards.add(new Commodity("TELAS",CardType.Commodity.TELAS,"DERECHA"));
		}
		
		for (int i=1;i<=8;i++){
			CommodityCards.add(new Commodity("TRIGO",CardType.Commodity.TRIGO,"DERECHA"));
		}
		
		for (int i=1;i<=6;i++){
			CommodityCards.add(new Commodity("CONFLICTO",CardType.Commodity.CONFLICTO,"DERECHA"));
		}
		
		//Creo las cartas de Rol
		
		RoleCards.add(new Role("CAPITAN",CardType.Role.CAPITAN,CardType.Positioning.AFAVOR));
		RoleCards.add(new Role("CONTRAMAESTRE",CardType.Role.CONTRAMAESTRE,CardType.Positioning.AFAVOR));
		RoleCards.add(new Role("AMOTINADO",CardType.Role.AMOTINADO,CardType.Positioning.ENCONTRA));
		RoleCards.add(new Role("GRUMETE",CardType.Role.GRUMETE,CardType.Positioning.ENCONTRA));		
		RoleCards.add(new Role("MERCADER",CardType.Role.MERCADER,CardType.Positioning.NEUTRAL));
		RoleCards.add(new Role("ESTIBADOR",CardType.Role.ESTIBADOR,CardType.Positioning.NEUTRAL));
		
	}
	public void shuffle(){
		Random rand = new Random();
		int pos;
		Commodity auxCommodity;
		Role auxRole;
		
		//Barajo Las Mercancias
		for (int i=0;i<36;i++){			
			pos=rand.nextInt(36);
			auxCommodity=CommodityCards.get(pos);
			CommodityCards.set(pos,CommodityCards.get(i));
			CommodityCards.set(i,auxCommodity);
		}
		
		//Barajo cartas del rol menos la del capitan que se queda en posici�n 0
				for (int i=1;i<6;i++){
					pos=rand.nextInt(6);
					auxRole=RoleCards.get(pos);
					RoleCards.set(pos,RoleCards.get(i));
					RoleCards.set(i,auxRole);
				}
	}
	
	public List<Commodity> getCommodityCards(Integer num){
		List<Commodity> ret=new ArrayList<Commodity>();
		
		if (!CommodityCards.isEmpty())
			if(num>CommodityCards.size()){
				for (int i=0;i<CommodityCards.size();i++){
					ret.add(CommodityCards.get(i));
					CommodityCards.remove(i);		
				}
			}else{
				for (int i=0;i<num;i++){
					ret.add(CommodityCards.get(i));
					CommodityCards.remove(i);		
				}
			}
		
		return ret;
	}
	
	//Te devuelve el objeto carta segun el nombre de la carta
	public Role getRole(String name) {
		Role card = null;
		name = name.toUpperCase();

		
		for (Role role : RoleCards) {
			if (role.getName().equals(name)) {
				card = role;
			}
		}
		

		return card;
	}
	
	public Island getIsland(String name){
		Island card=null;
		name = name.toUpperCase();
		for(Island island: getIslandCards()) {
			if (island.getName().equals(name)) {
				card = island;
			}
		}
		return card;
	}
	public Commodity getCommodity(String name){
		Commodity card=null;
		name = name.toUpperCase();
		for (Commodity commodity : CommodityCards) {
			if (commodity.getName().equals(name)) {
				card = commodity;
			}
		}
		return card;
	}
	
	public void addCommodity(Commodity commodity){
		getCommodityCards().add(commodity);
	}
	public void addRole(Role role){
		RoleCards.add(role);
	}
	
	
	public List<Commodity> getCommodityCards() {
		return CommodityCards;
	}

	
	public List<Island> getIslandCards() {
		return IslandCards;
	}


	public List<Role> getRoleCards() {
		return RoleCards;
	}


	
	
}
