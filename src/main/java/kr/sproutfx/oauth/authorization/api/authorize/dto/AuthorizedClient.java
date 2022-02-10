package kr.sproutfx.oauth.authorization.api.authorize.dto;

import java.util.UUID;

import kr.sproutfx.oauth.authorization.api.client.enumeration.ClientStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuthorizedClient {
    private UUID id;
    private String code;
    private String name;
    private ClientStatus status;
    private String description;
}