package com.cotato.weather.domain.place.entity;

import com.cotato.weather.domain.common.entity.BaseTime;
import com.cotato.weather.domain.user.entity.User;
import com.cotato.weather.global.converter.BooleanToYNConverter;
import com.cotato.weather.global.exception.AppException;
import com.cotato.weather.global.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;


@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedPlace extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private User user;

	@Column(nullable = false)
	private String placeName;

	@Column(nullable = false)
	private Double x;

	@Column(nullable = false)
	private Double y;

	@Convert(converter = BooleanToYNConverter.class)
	@Column(nullable = false)
	private boolean isPinned;

	public void updatePin(User user) {
		validateUser(user);
		this.isPinned = !this.isPinned;
	}

	private void validateUser(User user) {
		boolean isValid = Objects.equals(this.user.getId(), user.getId());
		if (!isValid) {
			throw new AppException(ErrorCode.USER_NOT_AUTHORIZED_FOR_ACTION);
		}
	}


}
