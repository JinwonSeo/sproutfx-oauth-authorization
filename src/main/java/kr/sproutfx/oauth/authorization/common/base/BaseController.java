package kr.sproutfx.oauth.authorization.common.base;

public class BaseController {

    protected static class StructuredBody<T> extends BaseResponseBody<T> {
        public StructuredBody(T content) {
            super(content);
        }

        public static <T> StructuredBody<T> content(T content) {
            return new StructuredBody<>(content);
        }
    }

}
