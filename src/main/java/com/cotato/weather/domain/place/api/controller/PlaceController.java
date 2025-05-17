package com.cotato.weather.domain.place.api.controller;

import com.cotato.weather.domain.place.dto.request.SavedPlaceCreateRequest;
import com.cotato.weather.domain.place.dto.response.SavedPlaceListResponse;
import com.cotato.weather.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import com.cotato.weather.domain.place.service.PlaceService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class PlaceController {

	private final PlaceService placeService;

	//등록된 위치 상세 조회

	//유저의 등록된 모든 위치 조회
	@Operation(summary = "유저의 등록된 모든 위치 조회", description = "유저의 등록된 모든 위치 조회")
	@GetMapping("")
	public ApiResponse<List<SavedPlaceListResponse>> getSavedPlaces() {
		List<SavedPlaceListResponse> savedPlaces = placeService.getSavedPlaces();
		return ApiResponse.ok(savedPlaces);
	}

	//위치 저장
	@Operation(summary = "위치 저장", description = "위치 저장")
	@PostMapping("")
	public ApiResponse<Long> savePlace(@RequestBody SavedPlaceCreateRequest savedPlaceCreateRequest) {
		Long savedPlaceId = placeService.savePlace(savedPlaceCreateRequest);
		return ApiResponse.created(savedPlaceId);
	}

	//위치 핀 등록 및 해제
	@Operation(summary = "위치 핀 등록 및 해제", description = "위치 핀 등록 및 해제")
	@PostMapping("/pin/{savedPlaceId}")
	public ApiResponse<Long> savePlacePin(@PathVariable("savedPlaceId") Long savedPlaceId) {
		placeService.updatePin(savedPlaceId);
		return ApiResponse.created(savedPlaceId);
	}


	// 위치 삭제
	@Operation(summary = "위치 삭제", description = "위치 삭제")
	@DeleteMapping("/{savedPlaceId}")
	public ApiResponse<Void> deletePlace(@PathVariable("savedPlaceId") Long savedPlaceId) {
		placeService.deletePlace(savedPlaceId);
		return ApiResponse.noContent();
	}


}
