package kr.sproutfx.oauth.authorization.api.authorize.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder @Getter @Setter
public class ClientKeyWithAuthentication {
    private String clientKey;
    private String email;
    private String password;
}
