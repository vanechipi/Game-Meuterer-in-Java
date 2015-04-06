package dad.Command;

import dad.Game.Card;
import dad.Game.Game;
import dad.Game.Role;
import dad.server.Message;
import dad.server.Player;

public class ChooseRoleCommand  extends GameCommand {
	private String nameCard;

	public  ChooseRoleCommand(String command, Player cc) {
		super (command,cc);
	}

	@Override
	public String execute()
	{
		throw new UnsupportedOperationException("This is a game command, 'execute' needs a parameter of type Game");
	}

	@Override
	public String execute(Game game) {
		String message = null;
		String [] parts = command.split(" ");
			
		
			if (parts.length == 2) {
				this.nameCard=parts[1];
				if(!player.getIsLobby()) {                          	// Si NO est? en el lobby
					Role role= game.getDeck().getRole(nameCard);  	// Se obtiene el objeto tipo carta con el nombre especificado
					if(role!=null && player.getCharacter()==null) {
						player.setCharacter(role);                // Se actualiza el rol del jugador
						game.getDeck().getRoleCards().remove(role);   // Se quita el rol de la baraja
						message=Message.SUCCES_CHOOSE_CARD;
					}else {
						message=Message.ERROR_ROLE;
					}
				}else {
					message=Message.ERROR_LEAVE_ISNTROOM;
				}
			} else {
				message = "Se esperaban 2 parametros";
			}
		
		return message;
	}
}