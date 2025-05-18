package org.example;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.security.spec.KeySpec;
import java.util.Base64;

public class CryptoUtils {
    public static SecretKey generateKeyFromPassword(String password, String saltBase64) throws Exception {
        byte[] salt = Base64.getDecoder().decode(saltBase64);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);

        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    public static String generateSalt(){
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String generateIv(){
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return Base64.getEncoder().encodeToString(iv);
    }

    public static String encrypt(String plainText, SecretKey Key, String ivBase64) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(ivBase64));

        cipher.init(Cipher.ENCRYPT_MODE, Key, iv);
        byte[] cipherText = cipher.doFinal(plainText.getBytes());

        return Base64.getEncoder().encodeToString(cipherText);
    }

    public static String decrypt(String cipherTextBase64, SecretKey Key, String ivBase64) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(ivBase64));

        cipher.init(Cipher.DECRYPT_MODE, Key, iv);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(cipherTextBase64));

        return new String(decryptedBytes);
    }
}
