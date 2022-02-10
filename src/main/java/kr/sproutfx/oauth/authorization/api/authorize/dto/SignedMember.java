package kr.sproutfx.oauth.authorization.api.authorize.dto;

import java.util.UUID;

import kr.sproutfx.oauth.authorization.api.member.enumeration.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SignedMember {
    private UUID id;
    private String email;
    private String name;
    private String passwordExpired;
    private MemberStatus status;
    private String description;
}
