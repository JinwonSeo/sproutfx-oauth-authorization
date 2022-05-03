package kr.sproutfx.oauth.authorization.common.advisor;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import kr.sproutfx.oauth.authorization.common.base.BaseResponseBody;
import kr.sproutfx.oauth.authorization.common.exception.UnhandledException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestControllerAdvisor {
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<BaseResponseBody<Object>> exception(final Throwable t) {
        if (t instanceof BaseException) {
            return new ResponseEntity<>(new BaseResponseBody<>(t), ((BaseException) t).getHttpStatus());
        } else {
            return new ResponseEntity<>(new BaseResponseBody<>(new UnhandledException((t == null) ? null : t.getLocalizedMessage())), HttpStatus.BAD_REQUEST);
        }
    }
}