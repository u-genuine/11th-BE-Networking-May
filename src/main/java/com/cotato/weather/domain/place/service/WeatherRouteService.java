package com.cotato.weather.domain.place.service;

import com.cotato.weather.domain.place.dto.response.WeatherResponse;
import com.cotato.weather.domain.place.dto.vo.ApiLog;
import com.cotato.weather.domain.place.repository.ApiLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherRouteService {
    private final AccuWeatherService accuWeatherService;
    private final OpenWeatherMapService openWeatherMapService;
    private final ApiLogRepository apiLogRepository;


    public WeatherResponse getWeather(Long placeId) {
        String mainApiLogStatus = getMainApiStatus();

        if (mainApiLogStatus.equals("OK")) {
            // 메인 API가 정상일 때
            log.info("Route Service 메인 API openWeatherMap 호출");
            return openWeatherMapService.getWeather(placeId);
        } else {
            // 메인 API가 비정상일 때 -> 서브 API accuWeather 사용
            log.info("Route Service 서브 API accuWeather 호출");
            return accuWeatherService.getWeather(placeId);
        }
    }

    public WeatherResponse testWeather() {
        Long testPlaceId = 1L; // 테스트용 장소 ID
        String mainApiLogStatus = getMainApiStatus();

        if (mainApiLogStatus.equals("OK")) {
            // 메인 API가 정상일 때
            log.info("Route Service 메인 API openWeatherMap 호출");
            return openWeatherMapService.testWeather(testPlaceId);
        } else {
            // 메인 API가 비정상일 때 -> 서브 API accuWeather 사용
            log.info("Route Service 서브 API accuWeather 호출");
            return accuWeatherService.getWeather(testPlaceId);
        }
    }

    public WeatherResponse getWeatherRetry(Long placeId) {
        return accuWeatherService.getWeather(placeId);
    }

    private String getMainApiStatus() {
        //메인 API 최신 로그 조회
        ApiLog apiLog = apiLogRepository.findMainApiLogNowStatus();
        return apiLog.getStatus();
    }
}
