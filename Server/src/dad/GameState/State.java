package dad.GameState;

import dad.Game.Game;

public abstract class State {
	protected Game game;
	
	public State(Game round) {
		this.game = round;
	}
	
	public Game getGame(){
		return game;
	}

	public abstract void compute();
}
