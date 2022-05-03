package kr.sproutfx.oauth.authorization.api.project.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class ProjectNotFoundException extends BaseException {

    public ProjectNotFoundException() {
        super("project_not_found", "Project not found", HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
