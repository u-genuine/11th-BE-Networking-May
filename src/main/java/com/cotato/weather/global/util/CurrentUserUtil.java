package com.cotato.weather.global.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Component;

import com.cotato.weather.domain.user.entity.User;
import com.cotato.weather.domain.user.repository.UserRepository;
import com.cotato.weather.global.exception.AppException;
import com.cotato.weather.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CurrentUserUtil {

	private final UserRepository userRepository;

	/**
	 * 현재 로그인된 사용자 엔티티를 가져온다
	 * @return 현재 로그인된 사용자 엔티티
	 */
	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			DefaultOAuth2User oAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
			String email = (String) oAuth2User.getAttributes().get("email");

			return userRepository.findByEmail(email)
				.orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
		}
		throw new AppException(ErrorCode.USER_NOT_AUTHORIZED_FOR_ACTION);
	}
}
