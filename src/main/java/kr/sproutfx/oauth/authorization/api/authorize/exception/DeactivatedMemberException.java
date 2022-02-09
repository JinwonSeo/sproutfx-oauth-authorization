package kr.sproutfx.oauth.authorization.api.authorize.exception;

import org.springframework.http.HttpStatus;

import kr.sproutfx.oauth.authorization.common.exception.BaseException;

public class DeactivatedMemberException extends BaseException {

    public DeactivatedMemberException() {
        super("deactivated_member", "This member's status is 'deactivated'", HttpStatus.UNAUTHORIZED);
    }

}
