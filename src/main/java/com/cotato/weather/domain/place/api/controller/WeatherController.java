package com.cotato.weather.domain.place.api.controller;

import com.cotato.weather.domain.place.dto.response.WeatherResponse;
import com.cotato.weather.domain.place.service.OpenWeatherMapService;
import com.cotato.weather.domain.place.service.WeatherRouteService;
import com.cotato.weather.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/places")
public class WeatherController {
    private final WeatherRouteService weatherRouteService;

    @Operation(summary = "특정 장소의 날씨 조회", description = "특정 장소의 날씨를 조회합니다.")
    @GetMapping("/{placeId}/weather")
    public ApiResponse<WeatherResponse> getWeatherByPlaceId(@PathVariable("placeId") Long placeId) {
        return ApiResponse.ok(weatherRouteService.getWeather(placeId));
    }

    @GetMapping("/test/weather")
    public ApiResponse<WeatherResponse> testWeather() {
        return ApiResponse.ok(weatherRouteService.testWeather());
    }
}
