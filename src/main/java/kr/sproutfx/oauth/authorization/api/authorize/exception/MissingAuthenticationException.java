package kr.sproutfx.oauth.authorization.api.authorize.exception;

import org.springframework.http.HttpStatus;

import kr.sproutfx.oauth.authorization.common.base.BaseException;

public class MissingAuthenticationException extends BaseException {

    public MissingAuthenticationException() {
        super("missing_authentication", "Missing authentication", HttpStatus.BAD_REQUEST);
    }
    
}
