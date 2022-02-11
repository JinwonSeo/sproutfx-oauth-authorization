package kr.sproutfx.oauth.authorization.api.authorize.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ClientKeyWithAuthorizedClient {
    private String clientKey;
    private AuthorizedClient authorizedClient;
}
