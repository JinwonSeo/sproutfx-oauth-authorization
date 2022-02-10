package kr.sproutfx.oauth.authorization.api.authorize.service;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import kr.sproutfx.oauth.authorization.api.authorize.exception.BlockedClientException;
import kr.sproutfx.oauth.authorization.api.authorize.exception.BlockedMemberException;
import kr.sproutfx.oauth.authorization.api.authorize.exception.ClientAccessDeniedException;
import kr.sproutfx.oauth.authorization.api.authorize.exception.DeactivatedClientException;
import kr.sproutfx.oauth.authorization.api.authorize.exception.DeactivatedMemberException;
import kr.sproutfx.oauth.authorization.api.authorize.exception.EmailFormatMismatchException;
import kr.sproutfx.oauth.authorization.api.authorize.exception.MissingAuthenticationException;
import kr.sproutfx.oauth.authorization.api.authorize.exception.PendingApprovalClientException;
import kr.sproutfx.oauth.authorization.api.authorize.exception.PendingApprovalMemberException;
import kr.sproutfx.oauth.authorization.api.authorize.exception.TokenCreationFailedException;
import kr.sproutfx.oauth.authorization.api.authorize.exception.UnauthorizedException;
import kr.sproutfx.oauth.authorization.api.authorize.model.request.ClientKeyWithAuthentication;
import kr.sproutfx.oauth.authorization.api.authorize.model.request.ClientKeyWithRefreshToken;
import kr.sproutfx.oauth.authorization.api.authorize.model.response.ClientKeyWithSignedAuthorizeClient;
import kr.sproutfx.oauth.authorization.api.authorize.model.response.TokenWithSignedMember;
import kr.sproutfx.oauth.authorization.api.client.entity.Client;
import kr.sproutfx.oauth.authorization.api.client.enumeration.ClientStatus;
import kr.sproutfx.oauth.authorization.api.client.service.ClientService;
import kr.sproutfx.oauth.authorization.api.member.model.entity.Member;
import kr.sproutfx.oauth.authorization.api.member.model.enumeration.MemberStatus;
import kr.sproutfx.oauth.authorization.api.member.service.MemberService;
import kr.sproutfx.oauth.authorization.common.utility.RegexUtils;
import kr.sproutfx.oauth.authorization.configuration.crypto.CryptoUtils;
import kr.sproutfx.oauth.authorization.configuration.security.jwt.JwtProvider;

@Service
public class AuthorizeService {

    private final ClientService clientService;
    private final MemberService memberService;
    private final CryptoUtils cryptoUtils;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthorizeService(ClientService clientService, MemberService memberService, CryptoUtils cryptoUtils, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.clientService = clientService;
        this.memberService = memberService;
        this.cryptoUtils = cryptoUtils;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public ClientKeyWithSignedAuthorizeClient getAuthorize(@RequestParam String clientCode) {
        Client targetClient = this.clientService.findByCode(clientCode);
        
        if (Boolean.FALSE.equals(this.isValidatedClient(targetClient))) throw new ClientAccessDeniedException();

        return ClientKeyWithSignedAuthorizeClient.builder()
            .clientKey(this.cryptoUtils.encrypt(targetClient.getSecret()))
            .authorizedClient(targetClient)
            .build();
    }

    public TokenWithSignedMember postToken(ClientKeyWithAuthentication clientKeyWithAuthentication) {
        String clientKey = clientKeyWithAuthentication.getClientKey();
        String email = clientKeyWithAuthentication.getEmail();
        String password = clientKeyWithAuthentication.getPassword();

        if (StringUtils.isBlank(clientKey) || StringUtils.isBlank(email) || StringUtils.isBlank(password)) {
            throw new MissingAuthenticationException();
        }

        String clientSecret = this.cryptoUtils.decrypt(clientKey);
        Client targetClient = this.clientService.findBySecret(clientSecret);

        if (Boolean.FALSE.equals(this.isValidatedClient(targetClient))) {
            throw new ClientAccessDeniedException();
        }

        if (Boolean.FALSE.equals(RegexUtils.validateEmail(email))) {
            throw new EmailFormatMismatchException();
        }

        Member targetMember = this.memberService.findByEmail(email);

        if (!passwordEncoder.matches(password, targetMember.getPassword())) {
            throw new UnauthorizedException();
        }

        if (Boolean.FALSE.equals(this.isValidatedMember(targetMember))) {
            throw new UnauthorizedException();
        }

        return createTokenWithSignedMember(targetClient, targetMember);
    }

    public TokenWithSignedMember postRefresh(ClientKeyWithRefreshToken clientKeyWithRefreshToken) {
        String clientKey = clientKeyWithRefreshToken.getClientKey();
        String refreshToken = clientKeyWithRefreshToken.getRefreshToken();

        if (StringUtils.isBlank(clientKey) || StringUtils.isBlank(refreshToken)) {
            throw new MissingAuthenticationException();
        }

        String clientSecret = this.cryptoUtils.decrypt(clientKey);
        Client targetClient = this.clientService.findBySecret(clientSecret);

        if (Boolean.FALSE.equals(this.isValidatedClient(targetClient)))
        {
            throw new ClientAccessDeniedException();
        }

        if (Boolean.FALSE.equals(this.jwtProvider.validateToken(targetClient.getRefreshTokenSecret(), targetClient.getCode(), refreshToken))){
            throw new UnauthorizedException();
        }

        UUID memberId = UUID.fromString(this.jwtProvider.extractSubject(targetClient.getRefreshTokenSecret(), targetClient.getCode(), refreshToken));

        Member targetMember = this.memberService.findById(memberId);

        if (Boolean.FALSE.equals(this.isValidatedMember(targetMember))) {
            throw new UnauthorizedException();
        }

        return createTokenWithSignedMember(targetClient, targetMember);
    }

    private TokenWithSignedMember createTokenWithSignedMember(Client client, Member member) {
        String subject = member.getId().toString();
        String audience = client.getCode();
        
        String accessTokenSecret = client.getAccessTokenSecret();
        String refreshTokenSecret = client.getRefreshTokenSecret();

        long accessTokenValidityInSeconds = client.getAccessTokenValidityInSeconds();
        long refreshTokenValidityInSeconds = client.getRefreshTokenValidityInSeconds();

        String accessToken = this.jwtProvider.createToken(subject, audience, accessTokenSecret, accessTokenValidityInSeconds);
        String refreshToken = this.jwtProvider.createToken(subject, audience, refreshTokenSecret, refreshTokenValidityInSeconds);

        if (StringUtils.isBlank(accessToken) || StringUtils.isBlank(refreshToken)) {
            throw new TokenCreationFailedException();
        }

        return TokenWithSignedMember.builder()
            .tokenType(this.jwtProvider.getAuthorizationType())
            .accessToken(accessToken)
            .accessTokenExpiresIn(this.jwtProvider.extractExpiresInSeconds(accessTokenSecret, audience, accessToken))
            .refreshToken(refreshToken)
            .refreshTokenExpiresIn(this.jwtProvider.extractExpiresInSeconds(refreshTokenSecret, audience, refreshToken))
            .signedMember(member)
            .build();
    }

    private boolean isValidatedClient(Client client) {
        if (ClientStatus.ACTIVE.equals(client.getStatus())) {
            return true;
        } else if (ClientStatus.BLOCKED.equals(client.getStatus())) {
            throw new BlockedClientException();
        } else if (ClientStatus.DEACTIVATED.equals(client.getStatus())) {
            throw new DeactivatedClientException();
        } else if (ClientStatus.PENDING_APPROVAL.equals(client.getStatus())) {
            throw new PendingApprovalClientException();
        } else {
            return false;
        }
    }

    private boolean isValidatedMember(Member member) {
        if (MemberStatus.ACTIVE.equals(member.getStatus())) {
            return true;
        } else if (MemberStatus.BLOCKED.equals(member.getStatus())) {
            throw new BlockedMemberException();
        } else if (MemberStatus.DEACTIVATED.equals(member.getStatus())) {
            throw new DeactivatedMemberException();
        } else if (MemberStatus.PENDING_APPROVAL.equals(member.getStatus())) {
            throw new PendingApprovalMemberException();
        } else {
            return false;
        }
    }
}
