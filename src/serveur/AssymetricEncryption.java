package serveur;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;

public class AssymetricEncryption {

    private static final String ALGORITHM = "RSA";

    private byte[] global_privateKey;
    private byte[] global_publicKey;

    public AssymetricEncryption(String pub_file, String priv_file) throws Exception {

        String puk = FileManager.readFirstLine(pub_file);
        global_publicKey = Base64.getDecoder().decode(puk);

        String prk = FileManager.readFirstLine(priv_file);
        global_privateKey = Base64.getDecoder().decode(prk);
    }
    
    public AssymetricEncryption(byte[] pubkey) throws Exception {

       
        global_publicKey = pubkey;


    }   

    public static void main(String[] args) throws Exception {

        AssymetricEncryption ea = new AssymetricEncryption("pub.txt", "priv.txt");

        try {
            gen();

            byte[] encryptedData = ea.encrypt("petit test".getBytes());

            byte[] decryptedData = ea.decrypt(encryptedData);

            System.out.println(new String(decryptedData));

        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public byte[] encrypt(byte[] publicKey, byte[] inputData)
            throws Exception {

        PublicKey key = KeyFactory.getInstance(ALGORITHM)
                .generatePublic(new X509EncodedKeySpec(publicKey));

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.PUBLIC_KEY, key);

        byte[] encryptedBytes = cipher.doFinal(inputData);

        return encryptedBytes;
    }

    public byte[] encrypt(byte[] inputData)
            throws Exception {

        return encrypt(global_publicKey, inputData);
    }

    public String encrypt(String inputData)
            throws Exception {
        byte[] r = encrypt(global_publicKey, Base64.getDecoder().decode(inputData));

        return new String(r);
    }

    public byte[] decrypt(byte[] privateKey, byte[] inputData)
            throws Exception {

        PrivateKey key = KeyFactory.getInstance(ALGORITHM)
                .generatePrivate(new PKCS8EncodedKeySpec(privateKey));

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.PRIVATE_KEY, key);

        byte[] decryptedBytes = cipher.doFinal(inputData);

        return decryptedBytes;
    }

    public byte[] decrypt(byte[] inputData)
            throws Exception {

        return decrypt(global_privateKey, inputData);
    }

    public String decrypt(String inputData)
            throws Exception {
        byte[] r = decrypt(global_publicKey, Base64.getMimeDecoder().decode(inputData));
        String re = new String(r);

        return re;
    }

    public static void gen() throws NoSuchAlgorithmException, NoSuchProviderException, IOException {

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);

        KeyPair kp = keyGen.genKeyPair();

        byte[] publicKey = kp.getPublic().getEncoded();
        byte[] privateKey = kp.getPrivate().getEncoded();

        System.out.println("public bytes : " + publicKey);
        System.out.println("private bytes : " + privateKey);

        String pubKeyAsString = Base64.getEncoder().encodeToString(publicKey);
        String privKeyAsString = Base64.getEncoder().encodeToString(privateKey);

        System.out.println("public : " + pubKeyAsString);
        System.out.println("private : " + privKeyAsString);

        List<String> lpubkey = Arrays.asList(pubKeyAsString);
        List<String> lprivkey = Arrays.asList(privKeyAsString);

        FileManager.tofile("pub.txt", lpubkey);
        FileManager.tofile("priv.txt", lprivkey);

    }

    public static String crypt(String entree) {

        return entree;

    }

    public byte[] getGlobal_privateKey() {
        return global_privateKey;
    }

    public void setGlobal_privateKey(byte[] global_privateKey) {
        this.global_privateKey = global_privateKey;
    }

    public byte[] getGlobal_publicKey() {
        return global_publicKey;
    }

    public void setGlobal_publicKey(byte[] global_publicKey) {
        this.global_publicKey = global_publicKey;
    }

}
