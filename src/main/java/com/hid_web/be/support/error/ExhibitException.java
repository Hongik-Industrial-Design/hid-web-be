package com.hid_web.be.support.error;

public class ExhibitException extends RuntimeException {
    private ErrorCode errorCode;
    private String message;

    public ExhibitException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
