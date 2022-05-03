package kr.sproutfx.oauth.authorization.api.authorize.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class DeactivatedClientException extends BaseException {

    public DeactivatedClientException() {
        super("deactivated_client", "This client's status is 'deactivated'", HttpStatus.UNAUTHORIZED);
    }

}
