package kr.sproutfx.oauth.authorization.api.member.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberUpdate {
    private String email;
    private String name;
    private String description;
}
