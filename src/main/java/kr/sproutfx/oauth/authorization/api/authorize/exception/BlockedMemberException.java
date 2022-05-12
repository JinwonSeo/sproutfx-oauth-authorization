package kr.sproutfx.oauth.authorization.api.authorize.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class BlockedMemberException extends BaseException {

    public BlockedMemberException() {
        super("blocked_member", "Member status is 'blocked'", HttpStatus.UNAUTHORIZED);
    }

}
