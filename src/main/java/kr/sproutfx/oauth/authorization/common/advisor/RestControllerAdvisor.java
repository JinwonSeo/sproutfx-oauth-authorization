package kr.sproutfx.oauth.authorization.common.advisor;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import kr.sproutfx.oauth.authorization.common.base.BaseResponseBody;
import kr.sproutfx.oauth.authorization.common.exception.UnhandledException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerAdvisor {
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<BaseResponseBody<Object>> exception(final Throwable t) {
        if (t instanceof BaseException) {
            return ResponseEntity.status(((BaseException) t).getHttpStatus()).body(new BaseResponseBody<>(t));
        } else if (t instanceof Exception) {
            return ResponseEntity.badRequest().body(new BaseResponseBody<>(new UnhandledException(t.getLocalizedMessage())));
        } else {
            return ResponseEntity.internalServerError().body(new BaseResponseBody<>(new UnhandledException(t.getLocalizedMessage())));
        }
    }
}