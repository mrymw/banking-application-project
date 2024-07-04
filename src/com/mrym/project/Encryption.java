package com.mrym.project;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Encryption {
    private SecretKey secretKey;
    private int keySize = 128;
    private int tLen = 128;
    private Cipher encryptionCipher;
    private Cipher decryptionCipher;
    public void init() throws NoSuchAlgorithmException {
        KeyGenerator generator =KeyGenerator.getInstance("AES");
        generator.init(128);
        secretKey = generator.generateKey();
    }
    public String encrypt(String message) throws Exception {
        byte[] messageInBytes = message.getBytes();
        encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte [] encryptedBytes = encryptionCipher.doFinal(messageInBytes);
        return encode(encryptedBytes);
    }
    public String decrypt(String encryptMessage) throws Exception {
        byte [] messageInBytes = decode(encryptMessage);
        decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(tLen, encryptionCipher.getIV());
        decryptionCipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        byte[] decryptedBytes = decryptionCipher.doFinal(messageInBytes);
        return new String(decryptedBytes);

    }
    private String encode(byte [] data) {
        return Base64.getEncoder().encodeToString(data);
    }
    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    public static void main(String[] args) {
        try{
            Encryption encryption = new Encryption();
            encryption.init();
            String encryptMessage = encryption.encrypt("Maryam");
            String decryptMessage = encryption.decrypt(encryptMessage);
            System.out.println(encryptMessage);
            System.out.println(decryptMessage);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
