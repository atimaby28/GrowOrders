package org.example.groworders.common.exception;

import org.example.groworders.common.model.BaseResponseStatus;

public class ExceptionHandlerExceptionResolver extends BaseException {

    public ExceptionHandlerExceptionResolver(String message) {
        super(BaseResponseStatus.REQUIRED_FIELD_ERROR, message);
    }

    public ExceptionHandlerExceptionResolver(String message, Throwable cause) {
        super(BaseResponseStatus.REQUIRED_FIELD_ERROR, message, cause);
    }
}
