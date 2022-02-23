package kr.sproutfx.oauth.authorization.configuration.jpa.component;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class SignedMemberAuditorAware implements AuditorAware<UUID> {

    @Override
    public Optional<UUID> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (null == authentication 
                || !authentication.isAuthenticated() 
                || null == authentication.getPrincipal()
                || authentication.getPrincipal() instanceof String
                || "anonymousUser".equals(authentication.getPrincipal())) {

            return Optional.empty();
        }

        return Optional.of(UUID.fromString(((User) authentication.getPrincipal()).getUsername()));
    }
    
}
