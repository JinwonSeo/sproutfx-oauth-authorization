package kr.sproutfx.oauth.authorization.api.authorize.exception;

import org.springframework.http.HttpStatus;

import kr.sproutfx.oauth.authorization.common.base.BaseException;

public class ExtractSubjectFailedException extends BaseException {

    public ExtractSubjectFailedException() {
        super("extract_failed", "Extract subject failed.", HttpStatus.UNAUTHORIZED);
    }
    
}
