package kr.sproutfx.oauth.authorization.api.authorize.exception;

import org.springframework.http.HttpStatus;

import kr.sproutfx.oauth.authorization.common.base.BaseException;

public class ExtractExpiresInSecondsFailedException extends BaseException {

    public ExtractExpiresInSecondsFailedException() {
        super("extract_failed", "Extract expires in seconds failed.", HttpStatus.UNAUTHORIZED);
    }
    
}
