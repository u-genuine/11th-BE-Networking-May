package com.cotato.weather.domain.place.service;

import com.cotato.weather.domain.place.dto.vo.ApiStatus;
import com.cotato.weather.domain.place.repository.ApiLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherApiStatusCheckService {
    private final ApiLogRepository apiLogRepository;
    private final OpenWeatherMapService openWeatherMapService;

    // 0초에 시작하여 매 1분마다 실행
    @Scheduled(cron = "0 0/1 * * * ?")
    public void checkWeatherApiStatus() {
        log.info("Checking OpenWeatherMap API status...");
        ApiStatus apiStatus = openWeatherMapService.checkApiStatus();
        apiLogRepository.insertMainApiLog(apiStatus);
    }


}
