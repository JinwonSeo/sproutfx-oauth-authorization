package kr.sproutfx.oauth.authorization.api.member.exception;

import java.util.UUID;

import org.springframework.http.HttpStatus;

import kr.sproutfx.oauth.authorization.common.exception.BaseException;

public class MemberNotFoundException extends BaseException {

    public MemberNotFoundException() {
        super("member_not_found", "Member not found", HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public MemberNotFoundException(UUID id) {
        super("member_not_found", String.format("Member(%s) not found", id.toString()), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
