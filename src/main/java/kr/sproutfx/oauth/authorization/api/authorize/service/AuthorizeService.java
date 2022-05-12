package kr.sproutfx.oauth.authorization.api.authorize.service;

import kr.sproutfx.common.security.configuration.jwt.component.JwtProvider;
import kr.sproutfx.oauth.authorization.api.authorize.exception.*;
import kr.sproutfx.oauth.authorization.api.client.entity.Client;
import kr.sproutfx.oauth.authorization.api.client.enumeration.ClientStatus;
import kr.sproutfx.oauth.authorization.api.member.entity.Member;
import kr.sproutfx.oauth.authorization.api.member.enumeration.MemberStatus;
import kr.sproutfx.oauth.authorization.api.project.entity.Project;
import kr.sproutfx.oauth.authorization.api.project.enumeration.ProjectStatus;
import kr.sproutfx.oauth.authorization.configuration.crypto.CryptoUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthorizeService {

    private final CryptoUtils cryptoUtils;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthorizeService(CryptoUtils cryptoUtils, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.cryptoUtils = cryptoUtils;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public String decryptClientSecret(String encryptedClientSecret) {
        return this.cryptoUtils.decrypt(encryptedClientSecret);
    }

    public String encryptClientSecret(String clientSecret) {
        return this.cryptoUtils.encrypt(clientSecret);
    }

    public String getTokenType() {
        return this.jwtProvider.getAuthorizationType();
    }

    public String createToken(String subject, String audience, String secret, long validityInSeconds) {
        String token = this.jwtProvider.createToken(subject, audience, secret, validityInSeconds);

        if (StringUtils.isBlank(token)) {
            throw new TokenCreationFailedException();
        }

        return token;
    }

    public String extractSubject(String secret, String audience, String token) {
        String subject = this.jwtProvider.extractSubject(secret, audience, token);

        if (StringUtils.isBlank(token)) {
            throw new ExtractSubjectFailedException();
        }

        return subject;
    }

    public Long extractTokenExpiresInSeconds(String secret, String audience, String token) {
        Long expiresInSeconds = this.jwtProvider.extractExpiresInSeconds(secret, audience, token);

        if (expiresInSeconds.equals(-1L)) {
            throw new ExtractExpiresInSecondsFailedException();
        }

        return expiresInSeconds;
    }

    public Boolean validateToken(String secret, String audience, String token) {
        return this.jwtProvider.validateToken(secret, audience, token);
    }

    public void validateProjectStatus(Project project) {
        if (project == null) {
            throw new AccessDeniedException();
        }

        if ( ProjectStatus.BLOCKED == project.getStatus()) {
            throw new BlockedProjectException();
        } else if ( ProjectStatus.DEACTIVATED == project.getStatus()) {
            throw new DeactivatedProjectException();
        } else if ( ProjectStatus.PENDING_APPROVAL == project.getStatus()) {
            throw new PendingApprovalProjectException();
        } else if ( ProjectStatus.ACTIVE != project.getStatus()) {
            throw new AccessDeniedException();
        }
    }

    public void validateClientStatus(Client client) {
        if (client == null) {
            throw new AccessDeniedException();
        }

        if (ClientStatus.BLOCKED == client.getStatus()) {
            throw new BlockedClientException();
        } else if (ClientStatus.DEACTIVATED == client.getStatus()) {
            throw new DeactivatedClientException();
        } else if (ClientStatus.PENDING_APPROVAL == client.getStatus()) {
            throw new PendingApprovalClientException();
        } else if (ClientStatus.ACTIVE != client.getStatus()) {
            throw new AccessDeniedException();
        }
    }

    public void validateMemberStatus(Member member) {
        if (member == null) {
            throw new AccessDeniedException();
        }

        if (MemberStatus.BLOCKED == member.getStatus()) {
            throw new BlockedMemberException();
        } else if (MemberStatus.DEACTIVATED == member.getStatus()) {
            throw new DeactivatedMemberException();
        } else if (MemberStatus.PENDING_APPROVAL == member.getStatus()) {
            throw new PendingApprovalMemberException();
        } else if (MemberStatus.ACTIVE != member.getStatus()) {
            throw new AccessDeniedException();
        }
    }

    public void validateMemberPassword(Member member, String password) {
        if (member == null) {
            throw new UnauthorizedException();
        }

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new UnauthorizedException();
        } else if (member.getPasswordExpired().isBefore(LocalDateTime.now())) {
            throw new PasswordExpiredException();
        }
    }
}
