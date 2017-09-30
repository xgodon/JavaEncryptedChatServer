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
	    	byte[] sk;
	    	AssymetricEncryption ae = new AssymetricEncryption("pub.txt", "priv.txt");
	    	
	    	
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
	      
	        // Normalement le nom
	        string_retour = ae.decrypt(line);
	        
	        // le priv est faux mais ne sera pas utilisé, on veut juste decrypter avec la clef publique
	        AssymetricEncryption ae_client = new AssymetricEncryption("pub_"+string_retour+".txt", "priv.txt"); 
	        
	        // Construction de la clef commune secrete
	        sk = DHEncryption.generateCommonSecretKey(ae.getGlobal_privateKey(), ae_client.getGlobal_publicKey());
	        
	        String message = DHEncryption.encryptMessage(sk, message);
	        
	        
	        
            out.writeBytes("coucou");
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