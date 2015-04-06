package dad.Command;

import java.util.StringTokenizer;

import dad.server.Player;

public class CommandFactory {
	public static Command createCommand(String command, Player player) {
		Command res = new ErrorCommand("Error en la ejecución", player);
		StringTokenizer tokenizer = new StringTokenizer(command, " ");
		if (tokenizer.hasMoreTokens()) {
			String keyword = tokenizer.nextToken();
			if (keyword.equalsIgnoreCase("VER_COMANDOS")) {
				res = new ShowAvailableCommands(command, player);
			} else if (player.getCommandEnabled().contains(keyword.toUpperCase())) {
				if (keyword.equalsIgnoreCase("NUEVO_NOMBRE")) {
					res = new ChangeNamePlayerCommand(command, player);
				}
				if (keyword.equalsIgnoreCase("VER_JUGADORES")) {
					res = new ShowPlayerCommand(command, player);
				}
				if (keyword.equalsIgnoreCase("CREAR_PARTIDA")) {
					res = new CreateRoomCommand(command, player);
				}
				if (keyword.equalsIgnoreCase("VER_PARTIDAS")) {
					res = new ShowMatchCommand(command, player);
				}
				if (keyword.equalsIgnoreCase("UNIRSE_PARTIDA")) {
					res = new JoinRoomCommand(command, player);
				}
				if (keyword.equalsIgnoreCase("SALIR")) {
					res = new LeaveRoomCommand(command, player);
				}
				if (keyword.equalsIgnoreCase("SUELDO_CONTRAMAESTRE")) {
					res = new SalaryBoatswainCommand(command, player);
				}
				if (keyword.equalsIgnoreCase("ELEGIR_CARTA")) {
					res = new ChooseCardCommand(command, player);
				}
				if (keyword.equalsIgnoreCase("ELEGIR_ROL")) {
					res = new ChooseRoleCommand(command, player);
				}
				if (keyword.equalsIgnoreCase("PASAR")) {
					res = new PassCommand(command, player);
				}
				if (keyword.equalsIgnoreCase("DESCARTAR")) {
					res = new DiscardStevedoreCommand(command, player);
				}
			} else {
				res = new ErrorCommand("Comando no permitido", player);
			}
		} else {
			res = new ErrorCommand("Comando no reconocido", player);
		}
		return res;
	}
}
