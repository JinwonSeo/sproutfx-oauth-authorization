package kr.sproutfx.oauth.authorization.api.authorize.controller;

import java.util.UUID;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.sproutfx.oauth.authorization.api.authorize.exception.ClientAccessDeniedException;
import kr.sproutfx.oauth.authorization.api.authorize.exception.MemberAccessDeniedException;
import kr.sproutfx.oauth.authorization.api.authorize.exception.UnauthorizedException;
import kr.sproutfx.oauth.authorization.api.authorize.service.AuthorizeService;
import kr.sproutfx.oauth.authorization.api.client.entity.Client;
import kr.sproutfx.oauth.authorization.api.client.enumeration.ClientStatus;
import kr.sproutfx.oauth.authorization.api.client.service.ClientService;
import kr.sproutfx.oauth.authorization.api.member.entity.Member;
import kr.sproutfx.oauth.authorization.api.member.enumeration.MemberStatus;
import kr.sproutfx.oauth.authorization.api.member.service.MemberService;
import kr.sproutfx.oauth.authorization.common.dto.Response;
import kr.sproutfx.oauth.authorization.common.exception.InvalidArgumentException;
import kr.sproutfx.oauth.authorization.common.utility.ModelMapperUtils;

import lombok.Builder;
import lombok.Data;

@RestController
@RequestMapping("/")
public class AuthorizeController {
    private final AuthorizeService authorizeService;
    private final ClientService clientService;
    private final MemberService memberService;

    @Autowired
    public AuthorizeController(AuthorizeService authorizeService, ClientService clientService, MemberService memberService) {
        this.authorizeService = authorizeService;
        this.clientService = clientService;
        this.memberService = memberService;
    }

    @GetMapping("/authorize")
    public Response<GetAuthorizeResponse> getAuthorize(@RequestParam String clientCode) {

        Client authorizedClient = this.clientService.findByCode(clientCode);
        
        if (Boolean.FALSE.equals(this.authorizeService.isValidatedClient(authorizedClient))) {
            throw new ClientAccessDeniedException();
        }

        String encryptedClientSecret = this.authorizeService.encryptClientSecret(authorizedClient.getSecret());

        return new Response<>(GetAuthorizeResponse.builder()
            .encryptedClientSecret(encryptedClientSecret)
            .authorizedClient(ModelMapperUtils.defaultMapper().map(authorizedClient, AuthorizedClient.class))
            .build());
    }

    @PostMapping("/token")
    public Response<PostTokenResponse> postToken(@RequestBody @Validated PostTokenRequest postTokenRequest, Errors errors) {
        if (errors.hasErrors()) throw new InvalidArgumentException();

        String encryptedClientSecret = postTokenRequest.getEncryptedClientSecret();
        String email = postTokenRequest.getEmail();
        String password = postTokenRequest.getPassword();

        String decryptedClientSecret = this.authorizeService.decryptClientSecret(encryptedClientSecret);
        Client authorizedClient = this.clientService.findBySecret(decryptedClientSecret);

        if (Boolean.FALSE.equals(this.authorizeService.isValidatedClient(authorizedClient))) {
            throw new ClientAccessDeniedException();
        }

        Member signedMember = this.memberService.findByEmail(email);

        if (Boolean.FALSE.equals(this.authorizeService.isValidatedMember(signedMember))) {
            throw new MemberAccessDeniedException();
        }

        if (Boolean.FALSE.equals(this.authorizeService.isMatchesMemberPassword(signedMember, password))) {
            throw new UnauthorizedException();
        }

        String subject = signedMember.getId().toString();
        String audience = authorizedClient.getCode();

        String accessTokenSecret = authorizedClient.getAccessTokenSecret();
        long accessTokenValidityInSeconds = authorizedClient.getAccessTokenValidityInSeconds();

        String refreshTokenSecret = authorizedClient.getRefreshTokenSecret();
        long refreshTokenValidityInSeconds = authorizedClient.getRefreshTokenValidityInSeconds();

        String accessToken = this.authorizeService.createToken(subject, audience, accessTokenSecret, accessTokenValidityInSeconds);
        Long accessTokenExpiresInSeconds = this.authorizeService.extractTokenExpiresInSeconds(accessTokenSecret, audience, accessToken);

        String refreshToken = this.authorizeService.createToken(subject, audience, refreshTokenSecret, refreshTokenValidityInSeconds);
        Long refreshTokenExpiresInSeconds = this.authorizeService.extractTokenExpiresInSeconds(refreshTokenSecret, audience, refreshToken);

        return new Response<>(PostTokenResponse.builder()
            .tokenType(this.authorizeService.getTokenType())
            .accessToken(accessToken)
            .accessTokenExpiresIn(accessTokenExpiresInSeconds)
            .refreshToken(refreshToken)
            .refreshTokenExpiresIn(refreshTokenExpiresInSeconds)
            .signedMember(ModelMapperUtils.defaultMapper().map(signedMember, SignedMember.class))
            .build());
    }

    @PostMapping("/refresh")
    public Response<PostRefreshResponse> postRefresh(@RequestBody PostRefreshRequest postRefreshRequest, Errors errors) {
        if (errors.hasErrors()) throw new InvalidArgumentException();
        
        String encryptedClientSecret = postRefreshRequest.getEncryptedClientSecret();
        
        String decryptedClientSecret = this.authorizeService.decryptClientSecret(encryptedClientSecret);
        Client authorizedClient = this.clientService.findBySecret(decryptedClientSecret);

        if (Boolean.FALSE.equals(this.authorizeService.isValidatedClient(authorizedClient))) {
            throw new ClientAccessDeniedException();
        }

        String refreshToken = postRefreshRequest.getRefreshToken();
        String audience = authorizedClient.getCode();

        String accessTokenSecret = authorizedClient.getAccessTokenSecret();
        long accessTokenValidityInSeconds = authorizedClient.getAccessTokenValidityInSeconds();

        String refreshTokenSecret = authorizedClient.getRefreshTokenSecret();
        long refreshTokenValidityInSeconds = authorizedClient.getRefreshTokenValidityInSeconds();

        Long refreshTokenExpiresInSeconds = this.authorizeService.extractTokenExpiresInSeconds(refreshTokenSecret, audience, refreshToken);

        String subject = this.authorizeService.extractSubject(refreshTokenSecret, audience, refreshToken);

        Member signedMember = this.memberService.findById(UUID.fromString(subject));

        if (Boolean.FALSE.equals(this.authorizeService.isValidatedMember(signedMember))) {
            throw new MemberAccessDeniedException();
        }

        // TO-DO: refresh token의 남은 기간이 최초 발급 기간의 절반 미만인 경우 refresh token 새로 발급
        if (refreshTokenExpiresInSeconds < (refreshTokenValidityInSeconds * 0.5)) {
            refreshToken = this.authorizeService.createToken(subject, audience, refreshTokenSecret, refreshTokenValidityInSeconds);
            refreshTokenExpiresInSeconds = this.authorizeService.extractTokenExpiresInSeconds(refreshTokenSecret, audience, refreshToken);
        }

        String accessToken = this.authorizeService.createToken(subject, audience, accessTokenSecret, accessTokenValidityInSeconds);
        Long accessTokenExpiresInSeconds = this.authorizeService.extractTokenExpiresInSeconds(accessTokenSecret, audience, accessToken);

        return new Response<>(PostRefreshResponse.builder()
            .tokenType(this.authorizeService.getTokenType())
            .accessToken(accessToken)
            .accessTokenExpiresIn(accessTokenExpiresInSeconds)
            .refreshToken(refreshToken)
            .refreshTokenExpiresIn(refreshTokenExpiresInSeconds)
            .signedMember(ModelMapperUtils.defaultMapper().map(signedMember, SignedMember.class))
            .build());
    }

    @Builder
    @Data
    static class GetAuthorizeResponse {
        private String encryptedClientSecret;
        private AuthorizedClient authorizedClient;
    }

    @Data
    static class PostTokenRequest {
        @NotBlank
        private String encryptedClientSecret;
        @Email
        @NotBlank
        private String email;
        @NotBlank
        private String password;
    }

    @Builder
    @Data
    static class PostTokenResponse {
        private String tokenType;
        private String accessToken;
        private long accessTokenExpiresIn;
        private String refreshToken;
        private long refreshTokenExpiresIn;
        private SignedMember signedMember;
    }

    @Data
    static class PostRefreshRequest {
        @NotBlank
        private String encryptedClientSecret;
        @NotBlank
        private String refreshToken;
    }

    @Builder
    @Data
    static class PostRefreshResponse {
        private String tokenType;
        private String accessToken;
        private long accessTokenExpiresIn;
        private String refreshToken;
        private long refreshTokenExpiresIn;
        private SignedMember signedMember;
    }

    @Data
    static class AuthorizedClient {
        private UUID id;
        private String code;
        private String name;
        private ClientStatus status;
        private String description;
    }

    @Data
    static class SignedMember {
        private UUID id;
        private String email;
        private String name;
        private String passwordExpired;
        private MemberStatus status;
        private String description;
    }
}
