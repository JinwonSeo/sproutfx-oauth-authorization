package kr.sproutfx.oauth.authorization.configuration.crypto.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("sproutfx.crypto.key-store")
public class CryptoProperties {
    private String location;
    private String alias;
    private String secret;
    private String password;

    public String getLocation() {
        return location;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
