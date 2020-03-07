package com.example.demo.security;


import lombok.extern.slf4j.Slf4j;
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
@Component @Slf4j public class AttributeEncryptor implements AttributeConverter<String, String> {

    @Value("${spring.security.encryption.method}") private String AES;
    @Value("${spring.security.encryption.secret}") private String SECRET;
    @Value("${spring.security.encryption.iv}") private byte[] iv;
    @Value("${spring.security.encryption.secret.key.spec}") private String secretKeySpec;

    private Key key;
    private Cipher cipher;

    private void init() throws NoSuchPaddingException, NoSuchAlgorithmException {
        log.debug("{init start}");
        key = new SecretKeySpec(SECRET.getBytes(), secretKeySpec);
        cipher = Cipher.getInstance(AES);
        log.debug("{init end}");
    }

    @Override public String convertToDatabaseColumn(String attribute) {
        log.debug("{convertToDatabaseColumn start}");
        try {
            if (key == null || cipher == null) {
                this.init();
            }
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
            String encodeToString =
                Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
            log.debug("{convertToDatabaseColumn end}");
            return encodeToString;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            log.warn("{convertToDatabaseColumn exception} (e)" + e.toString());
            throw new IllegalStateException(e);
        }

    }

    @Override public String convertToEntityAttribute(String dbData) {
        log.debug("{convertToEntityAttribute start}");
        try {
            if (key == null || cipher == null) {
                this.init();
            }
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
            String string = new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
            log.debug("{convertToEntityAttribute end}");
            return string;
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            log.warn("{convertToEntityAttribute exception} (e)" + e.toString());
            throw new IllegalStateException(e);
        }
    }
}
