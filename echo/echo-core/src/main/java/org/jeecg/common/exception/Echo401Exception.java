package org.jeecg.common.exception;

/**
 * @Description: jeecg-boot自定义401异常
 * @author: jeecg-boot
 */
public class Echo401Exception extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public Echo401Exception(String message) {
        super(message);
    }

    public Echo401Exception(Throwable cause) {
        super(cause);
    }

    public Echo401Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
