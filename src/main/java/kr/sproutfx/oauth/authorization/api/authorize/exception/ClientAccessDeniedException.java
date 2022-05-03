package kr.sproutfx.oauth.authorization.api.authorize.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class ClientAccessDeniedException extends BaseException {

    public ClientAccessDeniedException() {
        super("access_denied", "Access denied.", HttpStatus.UNAUTHORIZED);
    }

}
