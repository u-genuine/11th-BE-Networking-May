package com.cotato.weather.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// Common
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-500", "서버 내부에서 에러가 발생하였습니다."),
	BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON-400","잘못된 요청입니다."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON-401","인증이 필요합니다."),
	FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON-403", "금지된 요청입니다."),

	// User 관련 에러
	USER_NOT_AUTHORIZED_FOR_ACTION(HttpStatus.UNAUTHORIZED, "USER-001", "인증이 필요합니다"),

	// Place 관련 에러

	// ..
	;

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
