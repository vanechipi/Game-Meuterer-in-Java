package dad.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

public class ClientMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ChatClient client = new ChatClient();
		
		//System.out.println("Indique la IP: ");
		//BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
	    //String ip;
	    
	    int port = 0;
		/*try {
			
			ip = bufferRead.readLine();
			System.out.println("Indique el puerto: ");
		    boolean correctPort = false;
		    while (!correctPort) {
		    	
		    	String strPort = bufferRead.readLine();
		    	try {
		    		port = Integer.valueOf(strPort);
		    		correctPort = true;
		    	}
		    	catch (NumberFormatException nexcp) {}
		    }*/
			try {
				//client.initConnection(ip,port);
				client.initConnection("127.0.0.1",6000);
				System.out.println("Conectado al Juego Amotinado.");
				
			} catch (UnknownHostException e) {
				System.out.println("Host desconocido");
			} catch (IOException e) {
				System.out.println("No es posible conectarse con el servidor");
			}
		/*} catch (IOException e) {
			System.out.println("Imposible obtener información de la consola");
		}*/
		
	}

}
