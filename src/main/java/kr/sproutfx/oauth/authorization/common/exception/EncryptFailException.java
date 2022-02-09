package kr.sproutfx.oauth.authorization.common.exception;

import org.springframework.http.HttpStatus;

public class EncryptFailException extends BaseException {

    public EncryptFailException() {
        super("encrypt_fail", "Failed to encrypt data.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
