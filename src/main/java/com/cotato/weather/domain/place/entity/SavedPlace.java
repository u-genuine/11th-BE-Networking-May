package com.cotato.weather.domain.place.entity;

import com.cotato.weather.domain.common.entity.BaseTime;
import com.cotato.weather.domain.user.entity.User;

import com.cotato.weather.domain.user.entity.UserTemp;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor @AllArgsConstructor @Builder
public class SavedPlace extends BaseTime {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	private UserTemp userTemp;

	@Column(nullable = false)
	private String placeName;

	@Column(nullable = false)
	private Double x;

	@Column(nullable = false)
	private Double y;

	@Column(nullable = false)
	private String isPinned;

	public void updatePin() {
		this.isPinned = this.isPinned.equals("Y") ? "N" : "Y";
	}


}
