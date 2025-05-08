package com.cotato.weather.domain.place.api.controller;

import com.cotato.weather.domain.place.dto.request.SavedPlaceCreateRequest;
import com.cotato.weather.domain.place.dto.response.SavedPlaceListResponse;
import com.cotato.weather.global.common.response.ApiResponse;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import com.cotato.weather.domain.place.service.PlaceService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/places")
public class PlaceController {

	private final PlaceService placeService;
	
	//등록된 위치 상세 조회

	//유저의 등록된 모든 위치 조회
	@GetMapping("/api/v1/places")
	public ApiResponse<List<SavedPlaceListResponse>> getSavedPlaces() {
		Long userId = 1L; // 임시 userId를 1로 설정
		List<SavedPlaceListResponse> savedPlaces =
				placeService.getSavedPlaces(userId)
				.stream()
				.map(SavedPlaceListResponse::from)
				.toList();

		return ApiResponse.ok(savedPlaces);
	}

	//위치 저장
	@PostMapping("api/v1/places")
	public ApiResponse<Long> savePlace(@RequestBody SavedPlaceCreateRequest savedPlaceCreateRequest) {
		Long savedPlaceId = placeService.savePlace(savedPlaceCreateRequest);
		return ApiResponse.created(savedPlaceId);
	}

	//위치 핀 등록 및 해제
	@PostMapping("/api/v1/places/pin/{savedPlaceId}")
	public ApiResponse<Long> savePlacePin(@Param("savedPlaceId") Long savedPlaceId) {
		placeService.updatePin(savedPlaceId);
		return ApiResponse.created(savedPlaceId);
	}


	// 위치 삭제
	@DeleteMapping("/api/v1/places/{savedPlaceId}")
	public ApiResponse<Void> deletePlace(@PathVariable("savedPlaceId") Long savedPlaceId) {
		placeService.deletePlace(savedPlaceId);
		return ApiResponse.noContent();
	}


}
