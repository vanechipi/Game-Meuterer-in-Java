package Exceptions;

/**
 * 
 * Excepcion lanzada cuando un jugador abandona la partida
 *
 */


public class PlayerLeavingGameException  extends RuntimeException {

	
	private static final long serialVersionUID = 1L;

	public PlayerLeavingGameException() {
		super();
	}

	public PlayerLeavingGameException(String t) {
		super(t);
	}	

}
