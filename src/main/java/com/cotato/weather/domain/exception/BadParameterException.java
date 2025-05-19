package com.cotato.weather.domain.exception;

import com.cotato.weather.global.exception.ErrorCode;

public class BadParameterException extends RuntimeException {
    private ErrorCode errorCode;
    public BadParameterException(String message) {
        super(message);
    }
    public BadParameterException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    public BadParameterException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
