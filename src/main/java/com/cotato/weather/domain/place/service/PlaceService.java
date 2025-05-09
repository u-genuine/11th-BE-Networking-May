package com.cotato.weather.domain.place.service;

import com.cotato.weather.domain.place.dto.request.SavedPlaceCreateRequest;
import com.cotato.weather.domain.place.entity.SavedPlace;
import com.cotato.weather.domain.place.repository.SavedPlaceRepository;
import com.cotato.weather.domain.user.entity.UserTemp;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {

	private final SavedPlaceRepository savedPlaceRepository;

	// 위치 저장
	public Long savePlace(SavedPlaceCreateRequest savedPlaceCreateRequest) {
		UserTemp user = new UserTemp(1L); //임시 사용자

		SavedPlace savedPlace = user.createSavePlace(savedPlaceCreateRequest.getPlaceName(), savedPlaceCreateRequest.getX(), savedPlaceCreateRequest.getY());

		return savedPlaceRepository.save(savedPlace).getId();
	}

	// 위치 목록 조회
	public List<SavedPlace> getSavedPlaces(Long userId) {
		return savedPlaceRepository.findByUserTemp_Id(userId);
	}

	// 위치 핀 등록 및 해제
	public void updatePin(Long savedPlaceId) {
		SavedPlace savedPlace = savedPlaceRepository.findById(savedPlaceId)
				.orElseThrow(() -> new IllegalArgumentException("SavedPlace not found with id: " + savedPlaceId));

		// 핀 등록된 경우 해제, 해제된 경우 등록
		UserTemp user = new UserTemp(1L);
		user.updatePin(savedPlace);

		savedPlaceRepository.save(savedPlace);
	}

	// 위치 삭제
	public void deletePlace(Long id) {
		savedPlaceRepository.deleteById(id);
	}
}
