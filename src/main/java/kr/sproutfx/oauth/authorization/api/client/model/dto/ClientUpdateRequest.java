package kr.sproutfx.oauth.authorization.api.client.model.dto;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import kr.sproutfx.oauth.authorization.common.model.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ClientUpdateRequest extends BaseEntity implements Serializable {
    
    private String name;
    @Min(3600) @Max(7200)
    private long accessTokenValidityInSeconds;
    @Min(3600) @Max(86400)
    private long refreshTokenValidityInSeconds;
    private String description;
}
