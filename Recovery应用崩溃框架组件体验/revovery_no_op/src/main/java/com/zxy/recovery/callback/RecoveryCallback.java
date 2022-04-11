package com.zxy.recovery.callback;

/**
 * RecoveryCallback
 *
 * @author:zhengxiaoyong
 * @since 2021-04-06
 */
public interface RecoveryCallback {

    /**
     * stackTrace
     *
     * @param stackTrace
     */
    void stackTrace(String stackTrace);

    /**
     * cause
     *
     * @param cause
     */
    void cause(String cause);

    /**
     * exception
     *
     * @param throwExceptionType throwExceptionType
     * @param throwClassName throwClassName
     * @param throwMethodName throwMethodName
     * @param throwLineNumber throwLineNumber
     */
    void exception(String throwExceptionType, String throwClassName, String throwMethodName, int throwLineNumber);

    /**
     * throwable
     *
     * @param throwable throwable
     */
    void throwable(Throwable throwable);
}
