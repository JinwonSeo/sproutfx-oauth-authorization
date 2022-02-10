package kr.sproutfx.oauth.authorization.api.client.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import kr.sproutfx.oauth.authorization.common.exception.BaseException;

public class ClientNotFoundException extends BaseException {

    public ClientNotFoundException() {
        super("client_not_found", "Client not found", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public ClientNotFoundException(UUID id) {
        super("client_not_found", String.format("Client not found. id: %s", id.toString()) , HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
