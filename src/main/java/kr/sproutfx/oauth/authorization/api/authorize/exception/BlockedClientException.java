package kr.sproutfx.oauth.authorization.api.authorize.exception;

import org.springframework.http.HttpStatus;

import kr.sproutfx.oauth.authorization.common.exception.BaseException;

public class BlockedClientException extends BaseException {

    public BlockedClientException() {
        super("blocked_client", "This client's status is 'blocked'", HttpStatus.UNAUTHORIZED);
    }
    
}
