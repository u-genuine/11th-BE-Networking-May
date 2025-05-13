package com.cotato.weather.domain.user.entity;

import com.cotato.weather.domain.common.entity.BaseTime;
import com.cotato.weather.domain.user.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class User extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false)
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Builder
	public User(String name, String email, Role role) {
		this.name = name;
		this.email = email;
		this.role = role;
	}

	public User update(String name) {
		this.name = name;
		return this;
	}

	public String getRoleKey() {
		return this.role.getKey();
	}
}
