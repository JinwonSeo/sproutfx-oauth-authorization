package kr.sproutfx.oauth.authorization.api.authorize.exception;

import org.springframework.http.HttpStatus;

import kr.sproutfx.oauth.authorization.common.exception.BaseException;

public class PasswordExpiredException extends BaseException {

    public PasswordExpiredException() {
        super("password_expired", "Member password expired.", HttpStatus.UNAUTHORIZED);
    }

}
