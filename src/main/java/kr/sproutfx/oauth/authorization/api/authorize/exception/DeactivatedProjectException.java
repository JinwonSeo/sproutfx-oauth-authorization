package kr.sproutfx.oauth.authorization.api.authorize.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class DeactivatedProjectException extends BaseException {
    public DeactivatedProjectException() {
        super("deactivated_project", "Project status is 'deactivated'", HttpStatus.UNAUTHORIZED);
    }
}
