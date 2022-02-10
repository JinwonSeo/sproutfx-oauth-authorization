package kr.sproutfx.oauth.authorization.api.authorize.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClientKeyWithAuthentication {
    private String clientKey;
    private String email;
    private String password;
}
