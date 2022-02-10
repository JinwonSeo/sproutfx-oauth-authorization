package kr.sproutfx.oauth.authorization.api.authorize.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AuthenticationWithSignedMember {
    private String tokenType;
    private String accessToken;
    private long accessTokenExpiresIn;
    private String refreshToken;
    private long refreshTokenExpiresIn;
    private SignedMember signedMember;
}
