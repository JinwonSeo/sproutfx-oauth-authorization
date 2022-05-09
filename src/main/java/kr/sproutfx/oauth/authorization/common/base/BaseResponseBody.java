package kr.sproutfx.oauth.authorization.common.base;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponseBody<T> {

    private boolean succeeded;
    private Object content;
    private Error error;

    public BaseResponseBody(T object) {
        if (object instanceof Exception) {
            this.setSucceeded(false);
            this.setError(object);
        } else {
            this.setSucceeded(true);
            this.setContent(object);
        }
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public Object getContent() {
        return this.content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Error getError() {
        return error;
    }

    public void setError(T object) {
        this.error = new Error(((BaseException) object).getValue(), ((BaseException) object).getReason());
    }

    static class Error {
        private String value;
        private String reason;

        public Error(String value, String reason) {
            this.setValue(value);
            this.setReason(reason);
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}