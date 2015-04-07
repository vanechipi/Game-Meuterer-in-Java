package dad.client;


import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerConnection implements Runnable {
	Socket serverSocket;
	private DataInputStream input;
	
	public ServerConnection (Socket serverSocket) {
		this.serverSocket = serverSocket;
	}

	@Override
	public void run() {
		try {
			input = new DataInputStream(serverSocket.getInputStream());
			while (serverSocket.isConnected()) {
				String serverMsg = input.readUTF();
				System.out.println(serverMsg);
			}
		} catch (IOException e) {}
		System.out.println("Desconectando del servidor");
	}
	
}
