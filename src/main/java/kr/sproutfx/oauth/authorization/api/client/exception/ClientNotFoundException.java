package kr.sproutfx.oauth.authorization.api.client.exception;

import org.springframework.http.HttpStatus;

import kr.sproutfx.oauth.authorization.common.base.BaseException;

public class ClientNotFoundException extends BaseException {

    public ClientNotFoundException() {
        super("client_not_found", "Client not found", HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
