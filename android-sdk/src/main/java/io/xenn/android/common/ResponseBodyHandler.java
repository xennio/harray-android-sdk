package io.xenn.android.common;

public interface ResponseBodyHandler<R> {

    R handle(String rawResponseBody);
}
