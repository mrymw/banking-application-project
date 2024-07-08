package com.mrym.project;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
public class Decryption {
    private SecretKey secretKey;
    private int keySize = 128;
    private int tLen = 128;
    private byte [] IV;
    public void initFromStrings(String key, String IV) {
        secretKey = new SecretKeySpec(decode(key), "AES");
        this.IV = decode(IV);
    }
    private byte[] decode(String data) {
        return Base64.getDecoder().decode(data);
    }
    public String decrypt(String encryptMessage) throws Exception {
        byte [] messageInBytes = decode(encryptMessage);
        Cipher decryptionCipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(tLen, IV);
        decryptionCipher.init(Cipher.DECRYPT_MODE, secretKey, spec);
        byte[] decryptedBytes = decryptionCipher.doFinal(messageInBytes);
        return new String(decryptedBytes);

    }
}
