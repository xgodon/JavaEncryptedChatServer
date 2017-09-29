package serveur;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.RenderingHints.Key;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Fabien-portable
 */
public class SymetricEncryption {

    byte[] data;
    byte[] result;
    SecretKey key;

    public SymetricEncryption() {

    }

    public SecretKey generateKey() {
        SecretKey k = null;
        try {
            KeyGenerator kg = KeyGenerator.getInstance("DES");
            k = kg.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return k;
    }

    public byte[] crypt(String msg) {
        byte[] crypted = null;
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, (java.security.Key) key);
            crypted = cipher.doFinal(msg.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return crypted;
    }

    public byte[] decrypt(byte[] stream) {
        byte[] crypted = null;
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            crypted = cipher.doFinal(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return crypted;
    }

    public String getStringDecrypt(byte[] stream) {
        return new String(this.decrypt(stream));
    }

    public String getStringCrypt(String stream) {
        return new String(this.crypt(stream));
    }

    public String getStringKey() {
        if (this.key == null) {
            this.key = generateKey();
        }
        String s = Base64.getEncoder().encodeToString(this.key.getEncoded());
        return s;
    }

    public boolean createFileKey() throws UnsupportedEncodingException {
        PrintWriter writer;
        if (this.key == null) {
            this.key = generateKey();
        }
        try {
            writer = new PrintWriter("C:/Users/Fabien-portable/Desktop/symetric_key.txt");
            writer.write(this.getStringKey());
            writer.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return (new File("C:/Users/Fabien-portable/Desktop/symetric_key.txt")).exists();
    }
    
    public void constructKey(String stream){
        byte[] decodedKey = Base64.getDecoder().decode(stream);
        this.key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES");
    }

}
