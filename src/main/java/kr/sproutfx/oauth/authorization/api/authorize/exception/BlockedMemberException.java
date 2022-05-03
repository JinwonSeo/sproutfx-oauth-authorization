package kr.sproutfx.oauth.authorization.api.authorize.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class BlockedMemberException extends BaseException {

    public BlockedMemberException() {
        super("blocked_member", "This member's status is 'blocked'", HttpStatus.UNAUTHORIZED);
    }

}
