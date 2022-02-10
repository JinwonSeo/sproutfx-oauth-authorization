package kr.sproutfx.oauth.authorization.api.authorize.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClientKeyWithSignedAuthorizedClient {
    private String clientKey;
    private AuthorizedClient authorizedClient;
}
