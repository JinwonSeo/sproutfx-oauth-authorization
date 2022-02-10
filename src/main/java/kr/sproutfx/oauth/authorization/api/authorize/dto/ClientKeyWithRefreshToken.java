package kr.sproutfx.oauth.authorization.api.authorize.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClientKeyWithRefreshToken {
    private String clientKey;
    private String refreshToken;
}
