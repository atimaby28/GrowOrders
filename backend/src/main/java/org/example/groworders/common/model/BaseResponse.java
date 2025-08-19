package org.example.groworders.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static org.example.groworders.common.model.BaseResponseStatus.SUCCESS;

/**
 * API 공통 응답 형식
 * @param <T> 응답 데이터 타입
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private boolean success; // 요청 성공 여부
    private String message;  // 메시지
    private Integer code;    // 응답 코드
    private T data;          // 응답 데이터

    // 성공 응답
    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse.<T>builder()
                .success(SUCCESS.isSuccess())
                .message(SUCCESS.getMessage())
                .code(SUCCESS.getCode())
                .data(data)
                .build();
    }

    // 성공 응답
    public static <T> BaseResponse<T> successMessage(String message) {
        return BaseResponse.<T>builder()
                .success(SUCCESS.isSuccess())
                .message(SUCCESS.getMessage())
                .code(SUCCESS.getCode())
                .build();
    }

    //실패 응답
    public static <T> BaseResponse<T> fail(BaseResponseStatus status, T data) {
        return BaseResponse.<T>builder()
                .success(status.isSuccess())
                .message(status.getMessage())
                .code(status.getCode())
                .data(data)
                .build();
    }

    // 실패 응답
    public static <T> BaseResponse<T> fail(String message) {
        return BaseResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
}
