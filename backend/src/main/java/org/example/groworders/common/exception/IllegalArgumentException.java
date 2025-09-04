package org.example.groworders.common.exception;

import org.example.groworders.common.model.BaseResponseStatus;

public class IllegalArgumentException extends BaseException {
    public IllegalArgumentException(BaseResponseStatus status, String message) {
        super(status, message);
    }

    public IllegalArgumentException(BaseResponseStatus status, String message, Throwable cause) {
        super(status, message, cause);
    }
}
