package kr.sproutfx.oauth.authorization.configuration.security.property;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("sproutfx.security.web.ignore")
public class WebSecurityIgnoreProperties {
    private final List<String> patterns;
    
    public WebSecurityIgnoreProperties(List<String> patterns) {
        this.patterns = patterns;
    }

    public List<String> getPatterns() {
        return patterns;
    }
}
