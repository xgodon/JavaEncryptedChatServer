package serveur;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConnectionsManager {


	    static final int PORT = 666;

	    public static void main(String args[]) throws Exception {
	    	
	        ServerSocket chaussette_serveur = null;
	        Socket chaussette_jettable = null;

	        try {
	        	chaussette_serveur = new ServerSocket(PORT);
	        } catch (IOException e) {
	            e.printStackTrace();

	        }
	        while (true) {
	            try {
	            	chaussette_jettable = chaussette_serveur.accept();
	            } catch (IOException e) {
	                System.out.println("I/O error: " + e);
	            }
	            // new thread for a client
	            System.out.println( "pre creation oreille");
	            new Connection(chaussette_jettable).run();
	            System.out.println( "creation oreille");
	        }
	    }
	}


