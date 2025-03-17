package com.hid_web.be.support.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // Common
    INVALID_INPUT_VALUE(400, "COMMON-001", "유효성 검증에 실패한 경우"),
    INTERNAL_SERVER_ERROR(500, "COMMON-002", "서버에서 처리할 수 없는 경우"),

    // Exhibit
    /**
     * 전시가 존재하지 않을 경우
     */
    EXHIBIT_NOT_FOUND(404, "EXHIBIT-001", "전시를 찾을 수 없는 경우"),
    /**
     * Admin 권한 확인
     */
    EXHIBIT_ACCESS_DENIED(403, "EXHIBIT-003", "전시에 대한 권한이 없는 경우"),
    /**
     * 외부 API인 S3 관련 트랜잭션 오류 시 전시 불가능 상태인 Fail로 변경
     */
    EXHIBIT_INVALID_STATUS(400, "EXHIBIT-004", "잘못된 전시 상태인 경우");

    private final int status;
    private final String code;
    private final String description;

    ErrorCode(int status, String code, String description) {
        this.status = status;
        this.code = code;
        this.description = description;
    }
}
