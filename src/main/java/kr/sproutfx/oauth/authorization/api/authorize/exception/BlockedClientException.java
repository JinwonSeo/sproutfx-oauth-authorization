package kr.sproutfx.oauth.authorization.api.authorize.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class BlockedClientException extends BaseException {

    public BlockedClientException() {
        super("blocked_client", "This client's status is 'blocked'", HttpStatus.UNAUTHORIZED);
    }
    
}
