package kr.sproutfx.oauth.authorization.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BaseException extends RuntimeException {
    private final String value;
    private final String reason;
    private final HttpStatus httpStatus;

    public BaseException(String value, String reason, HttpStatus httpStatus) {
        this.value = value;
        this.reason = reason;
        this.httpStatus = httpStatus;
    }
}
