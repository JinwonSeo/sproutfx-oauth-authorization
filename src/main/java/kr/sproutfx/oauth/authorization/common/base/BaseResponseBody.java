package kr.sproutfx.oauth.authorization.common.base;

public class BaseResponseBody<T> {

    private boolean succeeded;
    private Object result;
    private Error error;
        
    public BaseResponseBody(T object) {
        if (object instanceof Exception) {
            this.setSucceeded(false);
            this.setError(object);
        } else  {
            this.setSucceeded(true);
            this.setResult(object);
        }
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public void setSucceeded(boolean succeeded) {
        this.succeeded = succeeded;
    }

    public Object getResult() {
        return this.result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Error getError() {
        return error;
    }

    public void setError(T object) {
        this.error = new Error(((BaseException) object).getValue(), ((BaseException) object).getReason());
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