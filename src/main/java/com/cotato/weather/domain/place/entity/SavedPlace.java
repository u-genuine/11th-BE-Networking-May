package com.cotato.weather.domain.place.entity;

import com.cotato.weather.domain.common.entity.BaseTime;
import com.cotato.weather.domain.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class SavedPlace extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false)
	private String placeName;

	@Column(nullable = false)
	private Double latitude;

	@Column(nullable = false)
	private Double longitude;
}
