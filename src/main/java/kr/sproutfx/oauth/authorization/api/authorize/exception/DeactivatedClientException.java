package kr.sproutfx.oauth.authorization.api.authorize.exception;

import org.springframework.http.HttpStatus;

import kr.sproutfx.oauth.authorization.common.exception.BaseException;

public class DeactivatedClientException extends BaseException {

    public DeactivatedClientException() {
        super("deactivated_client", "This client's status is 'deactivated'", HttpStatus.UNAUTHORIZED);
    }

}
