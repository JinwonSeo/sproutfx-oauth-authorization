package kr.sproutfx.oauth.authorization.api.authorize.model.response;

import kr.sproutfx.oauth.authorization.api.client.model.entity.Client;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder @Getter @Setter
public class ClientKeyWithSignedAuthorizeClient {
    private String clientKey;
    private Client authorizedClient;
}
