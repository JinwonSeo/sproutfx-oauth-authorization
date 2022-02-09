package kr.sproutfx.oauth.authorization.api.member.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberCreate {
    private String email;
    private String name;
    private String password;
}
