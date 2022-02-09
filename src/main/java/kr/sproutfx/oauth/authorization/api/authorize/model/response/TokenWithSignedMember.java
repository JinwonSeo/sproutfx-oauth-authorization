package kr.sproutfx.oauth.authorization.api.authorize.model.response;

import kr.sproutfx.oauth.authorization.api.member.model.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder @Getter @Setter
public class TokenWithSignedMember {
    private String tokenType;
    private String accessToken;
    private long accessTokenExpiresIn;
    private String refreshToken;
    private long refreshTokenExpiresIn;
    private Member signedMember;
}
