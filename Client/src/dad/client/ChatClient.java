package dad.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient {
	
	private Socket socket;
	
	public ChatClient() {
	}

	public void initConnection(String ip, int port) throws UnknownHostException, IOException {
		socket = new Socket(ip, port);
		Thread inputThread = new Thread(new ServerConnection(socket));
		Thread consoleThread = new Thread(new ConsoleInteraction(socket));
		consoleThread.setDaemon(true);
		inputThread.start();
		consoleThread.start();
	}

}
