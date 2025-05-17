package com.cotato.weather.domain.place.service;

import com.cotato.weather.domain.place.dto.request.SavedPlaceCreateRequest;
import com.cotato.weather.domain.place.dto.response.SavedPlaceListResponse;
import com.cotato.weather.domain.place.entity.SavedPlace;
import com.cotato.weather.domain.place.repository.SavedPlaceRepository;
import com.cotato.weather.domain.user.entity.User;
import com.cotato.weather.global.util.CurrentUserUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {
	private final CurrentUserUtil currentUserUtil;
	private final SavedPlaceRepository savedPlaceRepository;

	// 위치 저장
	public Long savePlace(SavedPlaceCreateRequest savedPlaceCreateRequest) {
		User user = currentUserUtil.getCurrentUser();

		SavedPlace savedPlace = SavedPlace.builder()
				.user(user)
				.placeName(savedPlaceCreateRequest.getPlaceName())
				.x(savedPlaceCreateRequest.getX())
				.y(savedPlaceCreateRequest.getY())
				.isPinned(false) // 기본값 false로 설정
				.build();


		return savedPlaceRepository.save(savedPlace).getId();
	}

	// 위치 목록 조회
	public List<SavedPlaceListResponse> getSavedPlaces() {
		User user = currentUserUtil.getCurrentUser();
		List<SavedPlace> savedPlaces =  savedPlaceRepository.findByUserId(user.getId());

		return savedPlaces.stream()
				.map(SavedPlaceListResponse::from)
				.toList();
	}

	// 위치 핀 등록 및 해제
	public void updatePin(Long savedPlaceId) {
		SavedPlace savedPlace = savedPlaceRepository.findById(savedPlaceId)
				.orElseThrow(() -> new IllegalArgumentException("SavedPlace not found with id: " + savedPlaceId));

		// 핀 등록된 경우 해제, 해제된 경우 등록
		User user = currentUserUtil.getCurrentUser();

		savedPlace.updatePin(user);


		savedPlaceRepository.save(savedPlace);
	}

	// 위치 삭제
	public void deletePlace(Long id) {
		savedPlaceRepository.deleteById(id);
	}

	//Id로 위치 조회
	public SavedPlace getSavedPlace(Long id) {
		return savedPlaceRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("SavedPlace not found with id: " + id));
	}


}
