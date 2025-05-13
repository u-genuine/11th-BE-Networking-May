package com.cotato.weather.global.config.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.cotato.weather.domain.user.enums.Role;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

	private final CustomOAuth2UserService customOAuth2UserService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.csrf(csrf -> csrf.disable())
			.headers(headers -> headers
				.frameOptions(frameOptions -> frameOptions.disable())
			)
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
				.requestMatchers("/api/v1/**").hasRole(Role.USER.name())
				.anyRequest().authenticated()
			)
			.logout(logout -> logout.logoutSuccessUrl("/"))
			.oauth2Login(oauth2 -> oauth2
				.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
			);

		return http.build();
	}
}
