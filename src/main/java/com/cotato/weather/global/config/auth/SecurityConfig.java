package com.cotato.weather.global.config.auth;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
			.cors(Customizer.withDefaults())
			.csrf(csrf -> csrf.disable())
			.headers(headers -> headers
				.frameOptions(frameOptions -> frameOptions.disable())
			)
			.authorizeHttpRequests(auth -> auth
				// .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
				// .requestMatchers("/api/v1/**").hasRole(Role.USER.name())
				// .anyRequest().authenticated()
				.requestMatchers("/**").permitAll()
			)
			.logout(logout -> logout.logoutSuccessUrl("/"));
			// .oauth2Login(oauth2 -> oauth2
			// 	.userInfoEndpoint(userInfo -> userInfo.userService(customOAuth2UserService))
			// 	.successHandler((request, response, authentication) -> {
			// 		// 로그인 성공 후 프론트엔드로 리디렉션
			// 		String redirectUri = "http://localhost:5173";
			// 		response.sendRedirect(redirectUri);
			// 	})
			// )


		return http.build();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("http://localhost:5173")); // 프론트 주소
		configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(List.of("*"));
		configuration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
