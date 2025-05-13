package com.cotato.weather.global.config.auth.dto;

import java.io.Serializable;

import com.cotato.weather.domain.user.entity.User;

import lombok.Getter;

@Getter
public class SessionUser implements Serializable {
	private String name;
	private String email;

	public SessionUser(User user) {
		this.name = user.getName();
		this.email = user.getEmail();
	}
}
