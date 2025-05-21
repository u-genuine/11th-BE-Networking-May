package com.cotato.weather.domain.exception;

import com.cotato.weather.global.exception.ErrorCode;


public class JsonParsingException extends RuntimeException {
    private final ErrorCode errorCode;
    public JsonParsingException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public JsonParsingException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
