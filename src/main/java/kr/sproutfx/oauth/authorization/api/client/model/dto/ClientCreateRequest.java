package kr.sproutfx.oauth.authorization.api.client.model.dto;

import javax.validation.constraints.NotBlank;

import kr.sproutfx.oauth.authorization.common.model.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClientCreateRequest extends BaseEntity {
    
    @NotBlank
    private String name;
    private String description;
}
