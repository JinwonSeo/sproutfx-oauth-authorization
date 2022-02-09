package kr.sproutfx.oauth.authorization.configuration.security.property;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("sproutfx.security.http.authorize-requests.permit-all")
public class HttpSecurityPermitAllProperties {
    private final List<String> patterns;
    
    public HttpSecurityPermitAllProperties(List<String> patterns) {
        this.patterns = patterns;
    }

    public List<String> getPatterns() {
        return patterns;
    }
}
