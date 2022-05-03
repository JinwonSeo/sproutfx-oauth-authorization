package kr.sproutfx.oauth.authorization.api.authorize.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class PendingApprovalClientException extends BaseException {

    public PendingApprovalClientException() {
        super("pending_approval_client", "This client's status is 'pending approval'", HttpStatus.UNAUTHORIZED);
    }

}
