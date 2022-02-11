package kr.sproutfx.oauth.authorization.common.dto;

import kr.sproutfx.oauth.authorization.common.exception.BaseException;

public class Response<T> {
    private boolean succeeded;
    private T result;
    private Error error;

    public Response(T result) {
        if (result instanceof BaseException) {
            this.setSucceeded(false);
            this.setError(result);
        } else {
            this.setSucceeded(true);
            this.setResult(result);
        }
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public Error getError() {
        return error;
    }

    public void setError(T result) {
        this.error = new Error(((BaseException) result).getValue(), ((BaseException) result).getReason());
    }

    class Error {
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

