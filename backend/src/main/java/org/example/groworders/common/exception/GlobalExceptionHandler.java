package org.example.groworders.common.exception;

import org.example.groworders.common.model.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.HashMap;
import java.util.Map;

import static org.example.groworders.common.model.BaseResponseStatus.REQUEST_ERROR;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private int httpStatusCodeMapper(int statusCode) {
        if (statusCode >= 40000) { return 500; }
        else { return 400;}
    }

    //클라이언트 요청 입력값 검증 예외처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Map<String, String>>> handleException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(400).body(BaseResponse.fail(REQUEST_ERROR, errors));
    }


    //서비스 예외처리
    @ExceptionHandler(BaseException.class)
    public ResponseEntity handleException(BaseException e) {
        return ResponseEntity.status(httpStatusCodeMapper(e.getStatus().getCode())).body(BaseResponse.fail(e.getStatus(), e.getMessage()));
    }
}
