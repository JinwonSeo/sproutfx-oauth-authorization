package kr.sproutfx.oauth.authorization.api.member.exception;

import org.springframework.http.HttpStatus;

import kr.sproutfx.oauth.authorization.common.exception.BaseException;

public class MemberPasswordNotMatchesException extends BaseException {

    public MemberPasswordNotMatchesException() {
        super("password_not_matches", "Member password not matches.", HttpStatus.UNAUTHORIZED);
    }

}
