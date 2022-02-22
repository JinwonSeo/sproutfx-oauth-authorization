package kr.sproutfx.oauth.authorization.api.project;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ProjectStatus {
    ACTIVE,
    PENDING_APPROVAL,
    DEACTIVATED,
    BLOCKED;

    @JsonCreator
    public static ProjectStatus fromValue(String value) {
        return ProjectStatus.valueOf(value.toUpperCase());
    }
}
