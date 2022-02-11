package kr.sproutfx.oauth.authorization.common.advisor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import kr.sproutfx.oauth.authorization.common.exception.BaseException;
import kr.sproutfx.oauth.authorization.common.exception.UnhandledException;
import kr.sproutfx.oauth.authorization.common.dto.Response;

@RestControllerAdvice
public class ControllerAdvisor {
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Response<Object>> exception(final Throwable t) {
        if (t instanceof BaseException) {
            return new ResponseEntity<>(new Response<>(t), ((BaseException) t).getHttpStatus());
        } else {
            return new ResponseEntity<>(new Response<>(new UnhandledException((t.getCause() == null) ? t.getLocalizedMessage() : t.getCause().getLocalizedMessage())), HttpStatus.BAD_REQUEST);
        }
    }
}