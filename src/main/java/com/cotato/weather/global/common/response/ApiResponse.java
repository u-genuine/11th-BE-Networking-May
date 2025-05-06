package com.cotato.weather.global.common.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> extends BaseResponse{

	private final T data;

	protected ApiResponse(HttpStatusCode status, T data) {
		super(status);
		this.data = data;
	}

	public static <T> ApiResponse<T> ok(T data) {
		return new ApiResponse<>(HttpStatus.OK, data);
	}

	public static <T> ApiResponse<T> created(T data) {
		return new ApiResponse<>(HttpStatus.CREATED, data);
	}

	public static <T> ApiResponse<T> noContent() {
		return new ApiResponse<>(HttpStatus.NO_CONTENT, null);
	}
}
