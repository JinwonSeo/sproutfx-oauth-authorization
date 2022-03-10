package kr.sproutfx.oauth.authorization.api.authorize.exception;

import org.springframework.http.HttpStatus;

import kr.sproutfx.oauth.authorization.common.base.BaseException;

public class UnauthorizedException extends BaseException {

    public UnauthorizedException() {
        super("unauthorized", "Incorrect authentication", HttpStatus.UNAUTHORIZED);
    }
    
}
