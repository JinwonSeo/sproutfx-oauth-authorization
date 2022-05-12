package kr.sproutfx.oauth.authorization.api.authorize.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class PendingApprovalProjectException extends BaseException {
    public PendingApprovalProjectException() {
        super("pending_approval_project", "Project status is 'pending approval'", HttpStatus.UNAUTHORIZED);
    }
}
