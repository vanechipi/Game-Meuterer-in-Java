package dad.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ConsoleInteraction implements Runnable {
	private DataOutputStream output;
	private Socket socket;
	
	public ConsoleInteraction(Socket socket){
		this.socket = socket;
	}
	@Override
	public void run() {
		boolean close = false;
		try {
			this.output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e1) {close = true;	}
		while (!close) {
			String msg = writeMessage();
			if (msg != null) {
				if (msg.equalsIgnoreCase("close"))
					close = true;
				else {
					try {
						output.writeUTF(msg);
					} catch (IOException e) {close = true;}
				}
			} else // if msg is null, then readLine failed
				close = true;
		}
		try {
			socket.close();
		} catch (IOException e) {		}
	}
	
	private String writeMessage() {
		String res = "";
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
	    boolean correctMessage = false;
	    while (!correctMessage) {
	    	try {
	    		res = bufferRead.readLine();
	    		correctMessage = true;
	    	} catch (IOException exc) {}
	    }
	    
	    return res;   
	}

}
