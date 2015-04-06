package dad.server;

import java.io.IOException;
import java.util.List;

import Exceptions.PlayerLeavingGameException;
import dad.Command.CommandDispatcher;
import dad.Game.Commodity;
import dad.Game.Role;
import dad.Game.Room;


/**
 * 
 *  Todos los métodos lanzan las excepcion PlayerLeavingGameException
 *  Se utiliza cuando un jugador abandona la partida.
 *  En la Documentacion MEM006 
 *
 */



public class LPlayer implements IPlayer{
	public LPlayer() {

	}

	

	@Override
	public void inputProcessing() throws IOException {
		throw new PlayerLeavingGameException();
	}

	@Override
	public void receiveCards(List<Commodity> cards) {
		throw new PlayerLeavingGameException();
	}

	@Override
	public boolean playerLeaveLobby() {
		throw new PlayerLeavingGameException();
	}

	@Override
	public boolean close() {
		throw new PlayerLeavingGameException();
	}

	@Override
	public Commodity commodityOfHand(String name) {
		throw new PlayerLeavingGameException();
	}

	@Override
	public String getIp() {
		throw new PlayerLeavingGameException();
	}

	@Override
	public Role getCharacter() {
		throw new PlayerLeavingGameException();
	}

	@Override
	public void setCharacter(Role character) {
		throw new PlayerLeavingGameException();
	}

	@Override
	public Integer getTurn() {
		throw new PlayerLeavingGameException();
	}

	@Override
	public void setTurn(Integer turn) {
		throw new PlayerLeavingGameException();
	}

	@Override
	public String getName() {
		throw new PlayerLeavingGameException();
	}

	@Override
	public Integer getPoints() {
		throw new PlayerLeavingGameException();
	}

	@Override
	public void addPoints(Integer points) {
		throw new PlayerLeavingGameException();
	}

	@Override
	public void setName(String name) {
		throw new PlayerLeavingGameException();
	}

	@Override
	public DBServer getServer() {
		throw new PlayerLeavingGameException();
	}

	@Override
	public Boolean getIsLobby() {
		throw new PlayerLeavingGameException();
	}

	@Override
	public void setIsLobby(Boolean isLobby) {
		throw new PlayerLeavingGameException();
	}

	@Override
	public Room getRoom() {
		throw new PlayerLeavingGameException();
	}

	@Override
	public List<String> getCommandEnabled() {
		throw new PlayerLeavingGameException();
	}

	@Override
	public void setRoom(Room room) {
		throw new PlayerLeavingGameException();
	}

	@Override
	public List<Commodity> getHand() {
		throw new PlayerLeavingGameException();
	}

	@Override
	public void setHand(List<Commodity> hand) {
		throw new PlayerLeavingGameException();
	}

	@Override
	public boolean isPassState() {
		throw new PlayerLeavingGameException();
	}

	@Override
	public void setPassState(boolean pasState) {
		throw new PlayerLeavingGameException();
	}

	@Override
 	public void sendMessage(Message msg) {}

	@Override
	public void setCommandDispatcher(CommandDispatcher commandDispatcher) {
		throw new PlayerLeavingGameException();
	}
}
