package kr.sproutfx.oauth.authorization.api.authorize.exception;

import org.springframework.http.HttpStatus;

import kr.sproutfx.oauth.authorization.common.base.BaseException;

public class ClientAccessDeniedException extends BaseException {

    public ClientAccessDeniedException() {
        super("access_denied", "Access denied.", HttpStatus.UNAUTHORIZED);
    }

}
