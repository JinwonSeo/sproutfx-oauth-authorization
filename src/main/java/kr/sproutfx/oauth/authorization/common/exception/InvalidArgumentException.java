package kr.sproutfx.oauth.authorization.common.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidArgumentException extends BaseException {

    public InvalidArgumentException() {
        super("invalid_argument", "Invalid argument.", HttpStatus.BAD_REQUEST);
    }

    public InvalidArgumentException(String reason) {
        super("invalid_argument", reason, HttpStatus.BAD_REQUEST);
    }
}
