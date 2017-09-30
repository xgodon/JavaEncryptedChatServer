package serveur;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Base64;

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
	      
	        // On attend normalement le nom en premier
	        // Que l'on décrypte avec notre clef privée
	        String provided_name = ae.decrypt(line);
	        
	        // On récupère la clef publique dans le fichier correspondant au nom 
	        String puk = FileManager.readFirstLine("pub_"+provided_name+".txt");
	        byte[] publicKey = Base64.getDecoder().decode(puk);
	        
	        
	        // Construction de la clef commune secrete avec notre clef privée et clef publique du partenaire
	        sk = DHEncryption.generateCommonSecretKey(ae.getGlobal_privateKey(), publicKey);
	        
	        // On encrypyte le message de retour avec la clef générée cis dessus
	        String message = DHEncryption.encryptMessage(sk, "Bien reçu");
	        
	        // On envoie le message
            out.writeBytes(message);
            out.flush();
	        
	        while (true) {
	            try {
	            	// On lit chaque réception de message
	                line = brinp.readLine();
	                // On la décode
	                line = DHEncryption.decryptMessage(sk, line);
	                if ((line == null) || line.equalsIgnoreCase("QUIT")) {
	                    socket.close();
	                    return;
	                } else {
	                	System.out.println("reçu : " + line);
	                	// On renvoie un message
	                    out.writeBytes(DHEncryption.encryptMessage(sk, "Bien reçu"));
	                    out.flush();
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	                return;
	            }
	        }
	    }
	}