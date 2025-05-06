package com.cotato.weather.global.common.response;

import org.springframework.http.HttpStatusCode;

import lombok.Getter;

@Getter
public class BaseResponse {

	private final int status;

	protected BaseResponse(HttpStatusCode status) {
		this.status = status.value();
	}
}
