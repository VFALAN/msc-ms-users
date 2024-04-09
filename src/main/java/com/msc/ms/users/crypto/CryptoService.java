package com.msc.ms.users.crypto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

@Service
public class CryptoService {
    @Value("${msc.security.algorithm}")
    private String ALGORITHM;
    @Value("${msc.security.key}")
    private String KEY;

    private SecretKey generateKey(String keyString) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        byte[] keyBytes = sha.digest(keyString.getBytes());
        keyBytes = Arrays.copyOf(keyBytes, 16); // Solo se toman los primeros 16 bytes para la clave de 128 bits
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    public String encrypt(String text) throws Exception {
        SecretKey secretKey = generateKey(KEY);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(text.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decrypt(String encryptedText) throws Exception {
        SecretKey secretKey = generateKey(KEY);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedBytes);
    }

}
