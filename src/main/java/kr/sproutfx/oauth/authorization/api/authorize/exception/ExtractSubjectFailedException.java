package kr.sproutfx.oauth.authorization.api.authorize.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class ExtractSubjectFailedException extends BaseException {

    public ExtractSubjectFailedException() {
        super("extract_failed", "Extract subject failed.", HttpStatus.UNAUTHORIZED);
    }

}
