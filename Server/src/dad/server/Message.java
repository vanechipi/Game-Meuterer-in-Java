package dad.server;


/**
 * 
 * 
 * 	Todos los mensajes que se muestran en el juego
 *
 */

public class Message {
	private String msg;
	
	
	public static final String ERROR_JOIN_FULL = "Error, la sala est� llena";
	public static final String ERROR_CREATE_EXISTROOM = "Error,ya existe la sala";
	public static final String ERROR_LEAVE_ISNTROOM = "Error,no est�s en ninguna sala";
	public static final String ERROR_JOIN_DONTEXISTROOM = "Error,la partida no existe";
	public static final String ERROR_ISNT_LOBBY = "Error,ya est�s en una partida,debes salirte.";
	public static final String ERROR_CHOOSEN_POINTS = "Error al elegir los puntos.Rango valido [0-3]";
	public static final String ERROR_DONT_PERMISSION = "Error, no tienes permiso para realizar este comando";
	public static final String ERROR_DONT_EXIT_CARD ="Error, la carta elegida no existe";
	public static final String ERROR_ISNT_GAME = "Error, no est�s en nig�n juego";
	public static final String ERROR_ROLE = "Error, no existe el rol o ya tienes uno";
	public static final String ERROR_DONT_ACTIVE_ISLAND = "Error, la isla elegida no esta activa";
	
	public static final String SUCCES_LEAVEROOM = "Has dejado la partida correctamente";
	public static final String SUCCES_JOINROOM = "Te has unido correctamente a la partida";
	public static final String SUCCES_CREATEROOM = "Partida creada correctamente";
	public static final String CHOOSE_POINTS = "Eres el capitan. Elige el sueldo del contramaestre ( 0, 1, 2 � 3)";
	public static final String IS_YOUR_TURN= "Es tu turno,elige una opci�n";
	public static final String SUCCES_CHOOSE_CARD ="Carta elegida correctamente";
	public static final String SUCCES_PASS ="Has pasado";
	public static final String WAIT= "Espere a la decisi�n del capitan";
	public static final String ESPACE="---------------------------------------";
	public static final String MISSED_TURN= "Lo siento, has perdido tu turno";
	
	public Message(String msg){
		this.msg=msg;
	}
	public String getMsg() {
		return msg;
	}
	public String toString () {
		return   msg;
	}
	
}
