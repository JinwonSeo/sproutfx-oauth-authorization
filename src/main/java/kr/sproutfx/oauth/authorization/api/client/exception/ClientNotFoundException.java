package kr.sproutfx.oauth.authorization.api.client.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class ClientNotFoundException extends BaseException {

    public ClientNotFoundException() {
        super("client_not_found", "Client not found", HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
