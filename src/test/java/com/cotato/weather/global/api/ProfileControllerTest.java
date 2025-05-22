package com.cotato.weather.global.api;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import org.junit.jupiter.api.Test;
import org.springframework.mock.env.MockEnvironment;

class ProfileControllerTest {

	@Test
	public void prod_profile이_조회된다() {
		// given
		String expectedProfile = "prod";
		MockEnvironment env = new MockEnvironment();
		env.addActiveProfile(expectedProfile);
		env.addActiveProfile("oauth");
		env.addActiveProfile("api-key");

		ProfileController controller = new ProfileController(env);

		// when
		String profile = controller.profile();

		// then
		assertThat(profile).isEqualTo(expectedProfile);
	}

	@Test
	public void prod_profile이_없으면_첫_번째가_조회된다() {
		// given
		String expectedProfile = "oauth";
		MockEnvironment env = new MockEnvironment();

		env.addActiveProfile(expectedProfile);
		env.addActiveProfile("api-key");

		ProfileController controller = new ProfileController(env);

		// when
		String profile = controller.profile();

		// then
		assertThat(profile).isEqualTo(expectedProfile);
	}

	@Test
	public void active_profile이_없으면_default가_조회된다() {
		// given
		String expectedProfile = "default";
		MockEnvironment env = new MockEnvironment();
		ProfileController controller = new ProfileController(env);

		// when
		String profile = controller.profile();

		// then
		assertThat(profile).isEqualTo(expectedProfile);
	}
}