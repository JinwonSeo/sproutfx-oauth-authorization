package kr.sproutfx.oauth.authorization.api.authorize.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class MissingAuthenticationException extends BaseException {

    public MissingAuthenticationException() {
        super("missing_authentication", "Missing authentication", HttpStatus.BAD_REQUEST);
    }
    
}
