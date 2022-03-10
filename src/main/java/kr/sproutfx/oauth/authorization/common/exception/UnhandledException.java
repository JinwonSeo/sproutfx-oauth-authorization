package kr.sproutfx.oauth.authorization.common.exception;

import kr.sproutfx.oauth.authorization.common.base.BaseException;

public class UnhandledException extends BaseException {

    public UnhandledException() {
        super("-1", "Unhandled exception", null);
    }

    public UnhandledException(String reason) {
        super("-1", reason, null);
    }
}
