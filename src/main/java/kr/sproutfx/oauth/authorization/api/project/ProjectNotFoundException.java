package kr.sproutfx.oauth.authorization.api.project;

import org.springframework.http.HttpStatus;

import kr.sproutfx.oauth.authorization.common.exception.BaseException;

public class ProjectNotFoundException extends BaseException {

    public ProjectNotFoundException() {
        super("project_not_found", "Project not found", HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
