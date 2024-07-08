package com.mrym.project;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class Encryption {
    private SecretKey secretKey;
    private int keySize = 128;
    private int tLen = 128;
    private byte [] IV;
    public void initFromStrings(String key, String IV) {
        secretKey = new SecretKeySpec(decode(key), "AES");
        this.IV = decode(IV);
    }
    public String encrypt(String message) throws Exception {
        byte[] messageInBytes = message.getBytes();
        Cipher encryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(tLen,IV);
        encryptionCipher.init(Cipher.ENCRYPT_MODE, secretKey, spec);
        byte [] encryptedBytes = encryptionCipher.doFinal(messageInBytes);
        return encode(encryptedBytes);
    }
    private String encode(byte [] data) {
        return Base64.getEncoder().encodeToString(data);
    }
    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }
    private void exportKeys(){
        System.err.println("SecretKey: " + encode(secretKey.getEncoded()));
        System.err.println("IV: " + encode(IV));
    }

}
