package kr.sproutfx.oauth.authorization.api.authorize.exception;

import org.springframework.http.HttpStatus;

import kr.sproutfx.oauth.authorization.common.exception.BaseException;

public class PendingApprovalClientException extends BaseException {

    public PendingApprovalClientException() {
        super("pending_approval_client", "This client's status is 'pending approval'", HttpStatus.UNAUTHORIZED);
    }

}
