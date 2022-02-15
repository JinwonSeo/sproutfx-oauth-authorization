package kr.sproutfx.oauth.authorization.api.authorize.exception;

import org.springframework.http.HttpStatus;

import kr.sproutfx.oauth.authorization.common.exception.BaseException;

public class ValidateFailException extends BaseException {

    public ValidateFailException() {
        super("validate_failed", "Validate failed.", HttpStatus.UNAUTHORIZED);
    }
    
}