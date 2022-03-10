package kr.sproutfx.oauth.authorization.api.authorize.exception;

import org.springframework.http.HttpStatus;

import kr.sproutfx.oauth.authorization.common.base.BaseException;

public class BlockedMemberException extends BaseException {

    public BlockedMemberException() {
        super("blocked_member", "This member's status is 'blocked'", HttpStatus.UNAUTHORIZED);
    }

}
