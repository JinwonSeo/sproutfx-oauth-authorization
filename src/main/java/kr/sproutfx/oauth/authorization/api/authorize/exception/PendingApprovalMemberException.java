package kr.sproutfx.oauth.authorization.api.authorize.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class PendingApprovalMemberException extends BaseException {

    public PendingApprovalMemberException() {
        super("pending_approval_member", "This member's status is 'pending approval'", HttpStatus.UNAUTHORIZED);
    }

}
