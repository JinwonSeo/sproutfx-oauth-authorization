package kr.sproutfx.oauth.authorization.api.authorize.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class ValidateFailException extends BaseException {

    public ValidateFailException() {
        super("validate_failed", "Validate failed.", HttpStatus.UNAUTHORIZED);
    }
    
}