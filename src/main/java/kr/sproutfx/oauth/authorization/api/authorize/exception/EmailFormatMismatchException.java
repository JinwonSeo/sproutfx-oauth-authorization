package kr.sproutfx.oauth.authorization.api.authorize.exception;

import org.springframework.http.HttpStatus;

import kr.sproutfx.oauth.authorization.common.base.BaseException;

public class EmailFormatMismatchException extends BaseException {

    public EmailFormatMismatchException() {
        super("email_format_mismatch", "Email format does not match", HttpStatus.BAD_REQUEST);
    }

}
