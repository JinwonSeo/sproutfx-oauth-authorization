package kr.sproutfx.oauth.authorization.api.authorize.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class BlockedProjectException extends BaseException {
    public BlockedProjectException() {
        super("blocked_project", "Project status is 'blocked'", HttpStatus.UNAUTHORIZED);
    }
}
