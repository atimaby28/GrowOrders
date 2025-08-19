package org.example.groworders.common.exception;

import lombok.*;
import org.example.groworders.common.model.BaseResponseStatus;

@Getter
public class BaseException extends RuntimeException{
    private BaseResponseStatus status;

    public BaseException(BaseResponseStatus status, String message) {
        super(message);
        this.status = status;
    }

    public static BaseException from(BaseResponseStatus status) {
        return new BaseException(status, status.getMessage());
    }
}
