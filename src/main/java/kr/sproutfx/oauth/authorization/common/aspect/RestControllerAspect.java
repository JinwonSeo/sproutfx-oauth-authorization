package kr.sproutfx.oauth.authorization.common.aspect;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Component
@Aspect
public class RestControllerAspect {

    class RestControllerLog {
        @JsonIgnore
        private final ObjectMapper objectMapper = new ObjectMapper();

        public RestControllerLog(String method, String request, Object[] args) {
            this.request = String.format("%s %s", method, request);
            this.setArguments(args);
        }

        private String request;
        private List<Object> arguments;

        public String getRequest() {
            return this.request;
        }

        public List<Object> getArguments() {
            return this.arguments;
        }

        private void setArguments(Object[] args) {
            for (Object object : args) {
                if (Boolean.TRUE.equals(this.canConvertToJsonString(object))) {
                    if (arguments == null) arguments = new ArrayList<>();

                    arguments.add(object);
                }
            }
        }

        private Boolean canConvertToJsonString(Object object) {
            try {
                this.objectMapper.writeValueAsString(object);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        public String toJsonString() throws JsonProcessingException {
            return this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        }
    }

    private final Logger logger = LoggerFactory.getLogger(RestControllerAspect.class);

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)"
        + " || @annotation(org.springframework.web.bind.annotation.PostMapping)"
        + " || @annotation(org.springframework.web.bind.annotation.PutMapping)"
        + " || @annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    private void onRequest() { /* Rest Controller Pointcut */ }

    @Around("onRequest()")
    public Object doLogging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String restControllerJsonLog = StringUtils.EMPTY;
        StopWatch stopWatch = new StopWatch();

        try {
            stopWatch.start();
            Object proceed = proceedingJoinPoint.proceed(proceedingJoinPoint.getArgs());
            stopWatch.stop();

            restControllerJsonLog = new RestControllerLog(httpServletRequest.getMethod(), httpServletRequest.getRequestURI(), proceedingJoinPoint.getArgs()).toJsonString();

            return proceed;
        } finally {
            logger.info("{} < {}({}ms)", restControllerJsonLog, httpServletRequest.getRemoteHost(), stopWatch.getTime());
        }
    }
}
