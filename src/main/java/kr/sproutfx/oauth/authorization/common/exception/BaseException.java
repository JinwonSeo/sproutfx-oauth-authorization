package kr.sproutfx.oauth.authorization.common.exception;

import org.springframework.http.HttpStatus;

public class BaseException extends RuntimeException {
    private final String value;
    private final String reason;
    private final HttpStatus httpStatus;

    public BaseException(String value, String reason, HttpStatus httpStatus) {
        this.value = value;
        this.reason = reason;
        this.httpStatus = httpStatus;
    }

    public String getValue() {
        return this.value;
    }

    public String getReason() {
        return this.reason;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
