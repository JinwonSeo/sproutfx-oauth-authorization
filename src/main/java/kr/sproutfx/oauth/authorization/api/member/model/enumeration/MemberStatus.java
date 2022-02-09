package kr.sproutfx.oauth.authorization.api.member.model.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MemberStatus {
    ACTIVE,
    PENDING_APPROVAL,
    DEACTIVATED,
    BLOCKED;

    @JsonCreator
    public static MemberStatus fromValue(String value) {
        return MemberStatus.valueOf(value.toUpperCase());
    }
}
