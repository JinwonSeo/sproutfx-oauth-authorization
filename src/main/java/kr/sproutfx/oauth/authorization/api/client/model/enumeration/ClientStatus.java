package kr.sproutfx.oauth.authorization.api.client.model.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ClientStatus {
    ACTIVE,
    PENDING_APPROVAL,
    DEACTIVATED,
    BLOCKED;

    @JsonCreator
    public static ClientStatus fromValue(String value) {
        return ClientStatus.valueOf(value.toUpperCase());
    }
}
