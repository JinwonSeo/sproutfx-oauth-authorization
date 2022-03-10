package kr.sproutfx.oauth.authorization.api.authorize.exception;

import org.springframework.http.HttpStatus;

import kr.sproutfx.oauth.authorization.common.base.BaseException;

public class TokenCreationFailedException extends BaseException {

    public TokenCreationFailedException() { 
        super("token_creation_failed", "Token creation failed", HttpStatus.FOUND);
    }
}
