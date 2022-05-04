package kr.sproutfx.oauth.authorization.api.authorize.controller;

import kr.sproutfx.oauth.authorization.api.authorize.service.AuthorizeService;
import kr.sproutfx.oauth.authorization.api.client.entity.Client;
import kr.sproutfx.oauth.authorization.api.client.service.ClientService;
import kr.sproutfx.oauth.authorization.api.member.entity.Member;
import kr.sproutfx.oauth.authorization.api.member.service.MemberService;
import kr.sproutfx.oauth.authorization.common.base.BaseController;
import kr.sproutfx.oauth.authorization.common.exception.InvalidArgumentException;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class AuthorizeController extends BaseController {
    private final AuthorizeService authorizeService;
    private final ClientService clientService;
    private final MemberService memberService;

    public AuthorizeController(AuthorizeService authorizeService, ClientService clientService, MemberService memberService) {
        this.authorizeService = authorizeService;
        this.clientService = clientService;
        this.memberService = memberService;
    }

    @Value("${sproutfx.security.authorization.refresh-token-secret}")
    private String refreshTokenSecret;

    @Value("${sproutfx.security.authorization.refresh-token-validity-in-seconds}")
    private Long refreshTokenValidityInSeconds;

    @GetMapping("/authorize")
    public Response<GetAuthorizeResponse> getAuthorize(@RequestParam String clientCode) {

        Client authorizedClient = this.clientService.findByCode(clientCode);

        this.authorizeService.validateClientStatus(authorizedClient);

        String encryptedClientSecret = this.authorizeService.encryptClientSecret(authorizedClient.getSecret());

        return new Response<>(GetAuthorizeResponse.builder()
            .encryptedClientSecret(encryptedClientSecret)
            .authorizedClient(new AuthorizedClient(authorizedClient))
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

        this.authorizeService.validateClientStatus(authorizedClient);

        Member signedMember = this.memberService.findByEmail(email);

        this.authorizeService.validateMemberStatus(signedMember);

        this.authorizeService.validateMemberPassword(signedMember, password);

        String subject = signedMember.getId().toString();
        String audience = authorizedClient.getCode();

        String accessTokenSecret = authorizedClient.getAccessTokenSecret();
        long accessTokenValidityInSeconds = authorizedClient.getAccessTokenValidityInSeconds();

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
            .signedMember(new SignedMember(signedMember))
            .build());
    }

    @PostMapping("/refresh")
    public Response<PostRefreshResponse> postRefresh(@RequestBody PostRefreshRequest postRefreshRequest, Errors errors) {
        if (errors.hasErrors()) throw new InvalidArgumentException();

        String encryptedClientSecret = postRefreshRequest.getEncryptedClientSecret();

        String decryptedClientSecret = this.authorizeService.decryptClientSecret(encryptedClientSecret);
        Client authorizedClient = this.clientService.findBySecret(decryptedClientSecret);

        this.authorizeService.validateClientStatus(authorizedClient);

        String refreshToken = postRefreshRequest.getRefreshToken();
        String audience = authorizedClient.getCode();

        String accessTokenSecret = authorizedClient.getAccessTokenSecret();
        long accessTokenValidityInSeconds = authorizedClient.getAccessTokenValidityInSeconds();

        Long refreshTokenExpiresInSeconds = this.authorizeService.extractTokenExpiresInSeconds(refreshTokenSecret, audience, refreshToken);

        String subject = this.authorizeService.extractSubject(refreshTokenSecret, audience, refreshToken);

        Member signedMember = this.memberService.findById(UUID.fromString(subject));

        this.authorizeService.validateMemberStatus(signedMember);

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
            .signedMember(new SignedMember(signedMember))
            .build());
    }

    @PutMapping("/password")
    public Response<MemberResponse> updateMemberPassword(@RequestBody @Validated UpdateMemberPasswordRequest updateMemberPasswordRequest, Errors errors) {
        if (errors.hasErrors()) throw new InvalidArgumentException();

        String email = updateMemberPasswordRequest.getEmail();
        String currentPassword = updateMemberPasswordRequest.getCurrentPassword();
        String newPassword = updateMemberPasswordRequest.getNewPassword();

        UUID id = this.memberService.updatePassword(email, currentPassword, newPassword);

        return new Response<>(new MemberResponse(this.memberService.findById(id)));
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
        private String status;
        private String description;

        public AuthorizedClient(Client client) {
            this.id = client.getId();
            this.code = client.getCode();
            this.name = client.getName();
            this.status = (client.getStatus() == null) ? null : client.getStatus().toString();
            this.description = client.getDescription();
        }
    }

    @Data
    static class SignedMember {
        private UUID id;
        private String email;
        private String name;
        private LocalDateTime passwordExpired;
        private String status;
        private String description;

        public SignedMember(Member member) {
            this.id = member.getId();
            this.email = member.getEmail();
            this.name = member.getName();
            this.passwordExpired = member.getPasswordExpired();
            this.status = (member.getStatus() == null) ? null : member.getStatus().toString();
            this.description = member.getDescription();
        }
    }

    @Data
    static class MemberResponse {
        private UUID id;
        private String email;
        private String name;
        private LocalDateTime passwordExpired;
        private String status;
        private String description;

        public MemberResponse(Member member) {
            this.id = member.getId();
            this.email = member.getEmail();
            this.name = member.getName();
            this.passwordExpired = member.getPasswordExpired();
            this.status = (member.getStatus() == null) ? null : member.getStatus().toString();
            this.description = member.getDescription();
        }
    }

    @Data
    static class UpdateMemberPasswordRequest {
        @NotBlank
        private String email;
        @NotBlank
        private String currentPassword;
        @NotBlank
        private String newPassword;
    }
}
