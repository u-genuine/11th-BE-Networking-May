package com.cotato.weather.global.common.response;

import org.springframework.http.HttpStatus;

import com.cotato.weather.global.exception.ErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;

/**
 * 에러 응답 클래스
 * 예외 발생 시 GlobalExceptionHandler에서 처리됨
 * GlobalExceptionHandler에서  ErrorResponse.of(errorCode, request)를 호출해서 에러 응답 생성
 * 생성된 응답은 클라이언트에게 HTTP 코드와 함께 전달됨
 */
@Getter
public class ErrorResponse extends BaseResponse {

	private final String code;
	private final String message;
	private final String method;
	private final String requestURI;

	private ErrorResponse(String code, String message, String method, String requestURI, HttpStatus httpStatus) {
		super(httpStatus);
		this.code = code;
		this.message = message;
		this.method = method;
		this.requestURI = requestURI;
	}

	public static ErrorResponse of(ErrorCode errorCode, HttpServletRequest request) {
		return new ErrorResponse(
			errorCode.getCode(),
			errorCode.getMessage(),
			request.getMethod(),
			request.getRequestURI(),
			errorCode.getHttpStatus()
		);
	}
}
