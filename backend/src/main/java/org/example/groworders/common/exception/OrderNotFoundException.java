package org.example.groworders.common.exception;

import org.example.groworders.common.model.BaseResponseStatus;



public class OrderNotFoundException extends BaseException {
    public OrderNotFoundException() {
        super(BaseResponseStatus.ORDER_NOT_FOUND);
    }

    public OrderNotFoundException(String message) {
        super(BaseResponseStatus.ORDER_NOT_FOUND, message);
    }
}
