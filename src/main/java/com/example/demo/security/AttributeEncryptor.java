package com.example.demo.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.persistence.AttributeConverter;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Attribute encryption
 *
 * @author diegotobalina
 */
@Component public class AttributeEncryptor implements AttributeConverter<String, String> {

    @Value("${spring.security.encryption.method}") private String AES;
    @Value("${spring.security.encryption.secret}") private String SECRET;
    @Value("${spring.security.encryption.iv}") private byte[] iv;

    private Key key;
    private Cipher cipher;

    private void init() throws NoSuchPaddingException, NoSuchAlgorithmException {
        key = new SecretKeySpec(SECRET.getBytes(), "AES");
        cipher = Cipher.getInstance(AES);
    }

    @Override public String convertToDatabaseColumn(String attribute) {
        try {
            if (key == null || cipher == null) {
                this.init();
            }
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override public String convertToEntityAttribute(String dbData) {
        try {
            if (key == null || cipher == null) {
                this.init();
            }
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            throw new IllegalStateException(e);
        }
    }
}
