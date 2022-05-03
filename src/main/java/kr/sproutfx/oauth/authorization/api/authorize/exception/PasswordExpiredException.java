package kr.sproutfx.oauth.authorization.api.authorize.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class PasswordExpiredException extends BaseException {

    public PasswordExpiredException() {
        super("password_expired", "Member password expired.", HttpStatus.UNAUTHORIZED);
    }

}
