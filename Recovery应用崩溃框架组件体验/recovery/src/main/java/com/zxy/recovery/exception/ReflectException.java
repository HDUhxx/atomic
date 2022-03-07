package com.zxy.recovery.exception;

/**
 * ReflectException
 *
 * @author:zhengxiaoyong
 * @since 2021-04-06
 */
public class ReflectException extends RuntimeException {
    /**
     * 构造
     */
    public ReflectException() {
        super();
    }

    /**
     * 构造
     *
     * @param detailMessage
     */
    public ReflectException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 构造
     *
     * @param detailMessage
     * @param throwable throwable
     */
    public ReflectException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    /**
     * 构造
     *
     * @param throwable throwable
     */
    public ReflectException(Throwable throwable) {
        super(throwable);
    }
}
