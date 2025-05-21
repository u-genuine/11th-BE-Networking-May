package com.cotato.weather.domain.exception;

import com.cotato.weather.global.exception.ErrorCode;

public class ExternalApiServerException extends RuntimeException {
    private final ErrorCode errorCode;

    public ExternalApiServerException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ExternalApiServerException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }


}
