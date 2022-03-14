package kr.sproutfx.oauth.authorization.common.advisor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.sproutfx.oauth.authorization.common.base.BaseException;
import kr.sproutfx.oauth.authorization.common.base.BaseResponse;
import kr.sproutfx.oauth.authorization.common.exception.UnhandledException;

@RestControllerAdvice
public class RestControllerAdvisor {
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<BaseResponse<Object>> exception(final Throwable t) {
        if (t instanceof BaseException) {
            return new ResponseEntity<>(new BaseResponse<>(t), ((BaseException) t).getHttpStatus());
        } else {
            return new ResponseEntity<>(new BaseResponse<>(new UnhandledException((t == null) ? null : t.getLocalizedMessage())), HttpStatus.BAD_REQUEST);
        }
    }
}