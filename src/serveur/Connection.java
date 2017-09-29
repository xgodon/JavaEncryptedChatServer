package serveur;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class Connection {

	    protected Socket socket;
	    static String name;
	    static String sym_key;
	    static SymetricEncryption se ;

	    public Connection(Socket clientSocket) {
	        this.socket = clientSocket;
	    }

	    public void run() throws Exception {
	    	
	    	AssymetricEncryption ae = new AssymetricEncryption();
	    	
	        InputStream inp = null;
	        BufferedReader brinp = null;
	        DataOutputStream out = null;
	        try {
	            inp = socket.getInputStream();
	            brinp = new BufferedReader(new InputStreamReader(inp));
	            out = new DataOutputStream(socket.getOutputStream());
	        } catch (IOException e) {
	            return;
	        }
	        String line;
	        String[] temp ;
	        byte[] retour;
	        String string_retour;
	        
	        line = brinp.readLine();
	      
	        string_retour = ae.decrypt(line);
	        temp = string_retour.split("|");   
	        name = temp[0];   
	        sym_key = temp[1];
	        
	        se = new SymetricEncryption();
	        
	        se.constructKey(sym_key);
	        
	        
            out.writeBytes(se.getStringCrypt("Ok"));
            out.flush();
	        
	        while (true) {
	            try {
	                line = brinp.readLine();
	                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
	                    socket.close();
	                    return;
	                } else {
	                	System.out.println(line);
	                    out.writeBytes(line + "\n\r");
	                    out.flush();
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	                return;
	            }
	        }
	    }
	}