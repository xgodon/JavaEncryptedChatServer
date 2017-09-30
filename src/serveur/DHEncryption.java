package serveur;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.KeyAgreement;

public class DHEncryption {
	
    public static byte[] generateCommonSecretKey(byte[] priv, byte[] pub) {
    
    	byte[] secretKey = null;
    	
        try {
        	
    	Key receivedPublicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(pub));
    	Key privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(priv));


            final KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
            keyAgreement.init(privateKey);
            keyAgreement.doPhase(receivedPublicKey, true);

             secretKey = shortenSecretKey(keyAgreement.generateSecret());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return secretKey;
    }
    

    
    private static byte[] shortenSecretKey(final byte[] longKey) {

        try {

            // Use 8 bytes (64 bits) for DES, 6 bytes (48 bits) for Blowfish
            final byte[] shortenedKey = new byte[8];

            System.arraycopy(longKey, 0, shortenedKey, 0, shortenedKey.length);

            return shortenedKey;

            // Below lines can be more secure
            // final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            // final DESKeySpec       desSpec    = new DESKeySpec(longKey);
            //
            // return keyFactory.generateSecret(desSpec).getEncoded();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    

}
