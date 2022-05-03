package kr.sproutfx.oauth.authorization.api.authorize.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class TokenCreationFailedException extends BaseException {

    public TokenCreationFailedException() { 
        super("token_creation_failed", "Token creation failed", HttpStatus.FOUND);
    }
}
