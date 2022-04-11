package com.zxy.recovery.exception;

/**
 * RecoveryException
 *
 * @author:zhengxiaoyong
 * @since 2021-04-06
 */
public class RecoveryException extends RuntimeException {
    /**
     * RecoveryException
     *
     * @param message
     */
    public RecoveryException(String message) {
        super(message);
    }

    /**
     * RecoveryException
     *
     * @param message
     * @param cause
     */
    public RecoveryException(String message, Throwable cause) {
        super(message, cause);
    }
}
