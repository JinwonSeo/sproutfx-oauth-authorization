package kr.sproutfx.oauth.authorization.configuration.crypto;

import kr.sproutfx.oauth.authorization.configuration.crypto.exception.EncryptFailedException;
import kr.sproutfx.oauth.authorization.configuration.crypto.property.CryptoProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;

@Component
public class CryptoUtils {
    CryptoProperties cryptoProperties;

    public CryptoUtils(CryptoProperties cryptoProperties) {
        this.cryptoProperties = cryptoProperties;
    }

    public String encrypt(String plainText) {
        try {
            String keyStoreLocation = this.cryptoProperties.getLocation().replace("classpath:/", StringUtils.EMPTY);
            ClassPathResource classPathResource = new ClassPathResource(keyStoreLocation);

            char[] keyPassword = this.cryptoProperties.getPassword().toCharArray();
            String alias = this.cryptoProperties.getAlias();

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(classPathResource.getInputStream(), keyPassword);

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            Certificate cert = keyStore.getCertificate(alias);

            cipher.init(Cipher.ENCRYPT_MODE, cert.getPublicKey());

            return Base64Utils.encodeToUrlSafeString(cipher.doFinal(plainText.getBytes()));
        } catch (Exception e) {
            throw new EncryptFailedException();
        }
    }

    public String decrypt(String base64EncodedEncryptedString) {
        try {
            String keyStoreLocation = this.cryptoProperties.getLocation().replace("classpath:/", StringUtils.EMPTY);
            char[] keyPassword = this.cryptoProperties.getPassword().toCharArray();
            String alias = this.cryptoProperties.getAlias();
            ClassPathResource classPathResource = new ClassPathResource(keyStoreLocation);
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(classPathResource.getInputStream(), keyPassword);

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");

            Key rsakey = keyStore.getKey(alias, keyPassword);

            cipher.init(Cipher.DECRYPT_MODE, rsakey);

            return new String(cipher.doFinal(Base64Utils.decodeFromUrlSafeString(base64EncodedEncryptedString)));
        } catch (Exception e) {
            e.printStackTrace();
            return StringUtils.EMPTY;
        }
    }
}
