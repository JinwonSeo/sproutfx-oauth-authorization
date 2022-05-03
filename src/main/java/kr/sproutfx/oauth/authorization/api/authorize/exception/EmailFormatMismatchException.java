package kr.sproutfx.oauth.authorization.api.authorize.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class EmailFormatMismatchException extends BaseException {

    public EmailFormatMismatchException() {
        super("email_format_mismatch", "Email format does not match", HttpStatus.BAD_REQUEST);
    }

}
