package fr.xen0xys.discordauth.common.encryption;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

public class Encryption {

    private boolean checkKey(@NotNull final String key) {
        if (key.length() < 8)
            throw new SecurityException("The key must be at least 8 characters long.");
        if (!Pattern.compile(".*[a-z].*").matcher(key).matches())
            throw new SecurityException("The key must contain at least one lowercase alphabetic character.");
        if (!Pattern.compile(".*[A-Z].*").matcher(key).matches())
            throw new SecurityException("The key must contain at least one uppercase alphabetic character.");
        if (!Pattern.compile(".*\\d.*").matcher(key).matches())
            throw new SecurityException("The key must contain at least one digit.");
        if (!Pattern.compile(".*[^a-zA-Z0-9].*").matcher(key).matches())
            throw new SecurityException("The key must contain at least one special character.");
        return true;
    }

    private SecretKeySpec getKey(String keyString) {
        if(!checkKey(keyString))
            throw new SecurityException("Key is not enough secure");
        byte[] keyBytes = new byte[16];
        byte[] originalKeyBytes = keyString.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(originalKeyBytes, 0, keyBytes, 0, Math.min(originalKeyBytes.length, keyBytes.length));
        return new SecretKeySpec(keyBytes, "AES");
    }

    @Nullable
    public String encryptSymmetric(@NotNull final String key, @NotNull final String data) {
        try {
            SecretKeySpec secretKeySpec = getKey(key);
            byte[] ivBytes = new byte[16];
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(ivBytes));
            byte[] encryptedMessage = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public String decryptSymmetric(String key, String encryptedData) {
        try {
            SecretKeySpec secretKeySpec = getKey(key);
            byte[] ivBytes = new byte[16];
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(ivBytes));
            byte[] decryptedMessage = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            return new String(decryptedMessage, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String hash(String data) {
        return BCrypt.withDefaults().hashToString(12, data.toCharArray());
    }

    public boolean compareHash(String data, String hashedData) {
        return BCrypt.verifyer().verify(data.toCharArray(), hashedData).verified;
    }
}
