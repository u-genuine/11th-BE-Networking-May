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
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-002", "사용자를 찾을 수 없습니다."),

	// Place 관련 에러

	// 외부 서버 에러
	EXTERNAL_API_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EXTERNAL-001", "외부 서버와의 통신에서 에러가 발생하였습니다."),
	EXTERNAL_API_SERVER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "EXTERNAL-002", "외부 API 서버와의 통신에서 인증 에러가 발생하였습니다."),
	EXTERNAL_API_SERVER_EXCEED_RATE_LIMIT(HttpStatus.TOO_MANY_REQUESTS, "EXTERNAL-003", "외부 API 서버와의 통신에서 요청 한도를 초과하였습니다."),
	EXTERNAL_API_SERVER_BAD_REQUEST(HttpStatus.BAD_REQUEST, "EXTERNAL-004", "외부 API 서버와의 통신에서 잘못된 요청이 발생하였습니다."),
	EXETRNAL_API_SERVER_NOT_FOUND_LAT_LON(HttpStatus.NOT_FOUND, "EXTERNAL-005", "외부 API 서버와의 통신에서 요청한 리소스를 찾을 수 없습니다."),

	// 파싱 에러
	PARSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "PARSING-001", "데이터 파싱 중 에러가 발생하였습니다."),
	;

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}
