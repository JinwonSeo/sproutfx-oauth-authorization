package kr.sproutfx.oauth.authorization.api.authorize.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class ExtractExpiresInSecondsFailedException extends BaseException {

    public ExtractExpiresInSecondsFailedException() {
        super("extract_failed", "Extract expires in seconds failed.", HttpStatus.UNAUTHORIZED);
    }

}
