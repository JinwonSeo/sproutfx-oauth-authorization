package kr.sproutfx.oauth.authorization.api.authorize.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import org.springframework.http.HttpStatus;

public class MemberAccessDeniedException extends BaseException {

    public MemberAccessDeniedException() {
        super("access_denied", "Access denied.", HttpStatus.UNAUTHORIZED);
    }

}