package kr.sproutfx.oauth.authorization.api.authorize.exception;

import org.springframework.http.HttpStatus;

import kr.sproutfx.oauth.authorization.common.base.BaseException;

public class MemberAccessDeniedException extends BaseException {

    public MemberAccessDeniedException() {
        super("access_denied", "Access denied.", HttpStatus.UNAUTHORIZED);
    }

}