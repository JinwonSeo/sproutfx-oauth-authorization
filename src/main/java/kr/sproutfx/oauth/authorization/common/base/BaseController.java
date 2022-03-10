package kr.sproutfx.oauth.authorization.common.base;

public class BaseController {
    
    protected class Response<T> extends BaseResponse<T> {

        public Response(T result) {
            super(result);
        }
    }
}
