package com.hid_web.be.support.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Domain 계층에서 HTTP status code에 의존하지 않도록 하여 역방향 참조를 막는다.
 */
@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final String code;
    private final String message;

    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode.getCode(), message);
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getCode(), errorCode.getDescription());
    }
}
