package com.cotato.weather.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

	USER("ROLE_USER", "일반 유저");

	private final String key;
	private final String title;
}
