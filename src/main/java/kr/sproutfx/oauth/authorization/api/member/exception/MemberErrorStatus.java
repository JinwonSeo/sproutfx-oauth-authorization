package kr.sproutfx.oauth.authorization.api.member.exception;

public enum MemberErrorStatus {
    MEMBER_NOT_FOUND("code", "Member not found.");

    private final String value;
    private final String reason;
    
    private MemberErrorStatus(String value, String reason) {
        this.value = value;
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public String getValue() {
        return value;
    }
}
