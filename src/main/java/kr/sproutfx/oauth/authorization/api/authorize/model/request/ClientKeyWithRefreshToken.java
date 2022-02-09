package kr.sproutfx.oauth.authorization.api.authorize.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder @Getter @Setter
public class ClientKeyWithRefreshToken {
    private String clientKey;
    private String refreshToken;
}
