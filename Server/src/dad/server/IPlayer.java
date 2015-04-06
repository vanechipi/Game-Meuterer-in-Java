package dad.server;

import java.io.IOException;
import java.util.List;

import dad.Command.CommandDispatcher;
import dad.Game.Commodity;
import dad.Game.Role;
import dad.Game.Room;

public interface IPlayer {
	void inputProcessing() throws IOException;
	void receiveCards(List<Commodity> cards);
	void sendMessage(Message msg);
	void setCommandDispatcher(CommandDispatcher commandDispatcher);
	boolean playerLeaveLobby();
	boolean close();
	Commodity commodityOfHand(String name);
	String getIp();
	Role getCharacter();
	void setCharacter(Role character);
	Integer getTurn();
	void setTurn(Integer turn);
	String getName();
	Integer getPoints();
	void addPoints(Integer points);
	void setName(String name);
	DBServer getServer();
	Boolean getIsLobby();
	void setIsLobby(Boolean isLobby);
	Room getRoom();
	List<String> getCommandEnabled();
	void setRoom(Room room);
	List<Commodity> getHand();
	void setHand(List<Commodity> hand);
	boolean isPassState();
	void setPassState(boolean pasState);
}
