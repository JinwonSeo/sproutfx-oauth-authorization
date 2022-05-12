package kr.sproutfx.oauth.authorization.api.authorize.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class DeactivatedMemberException extends BaseException {

    public DeactivatedMemberException() {
        super("deactivated_member", "Member status is 'deactivated'", HttpStatus.UNAUTHORIZED);
    }

}
