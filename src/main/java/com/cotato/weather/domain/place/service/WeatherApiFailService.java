package com.cotato.weather.domain.place.service;

import com.cotato.weather.domain.exception.ExternalApiServerException;
import com.cotato.weather.domain.exception.JsonParsingException;
import com.cotato.weather.domain.place.dto.response.WeatherResponse;
import com.cotato.weather.domain.place.repository.ApiLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class WeatherApiFailService {
    private final ApiLogRepository apiLogRepository;
    private final AccuWeatherService accuWeatherService;

    //외부 API 서버 장애발생시 호출됨
    //API 서버 ERROR 로그 저장 후 재시도
    public WeatherResponse failExternalApi(ExternalApiServerException e, Long placeId) {
        //API 서버 ERROR 로그 저장
        insertApiLog(e.getErrorCode().getCode(), e.getErrorCode().getMessage());

        //서브 API로 재시도
        return handleRetry(placeId);
    }

    //외부 API 서버는 정상 응답
    //JSON 파싱 에러 발생시 호출됨
    public WeatherResponse failJsonParsing(JsonParsingException e, Long placeId) {
        //API 서버 ERROR 로그 저장
        insertApiLog(e.getErrorCode().getCode(), e.getErrorCode().getMessage());

        //서브 API로 재시도
        return handleRetry(placeId);
    }


    private WeatherResponse handleRetry(Long placeId) {
        //서브 API로 재시도
        log.info("WeatherApiFailService handleRetry() 서브 API 호출");
        return accuWeatherService.getWeather(placeId);
    }

    private void insertApiLog(String code, String message) {
        apiLogRepository.insertMainApiLog("ERROR", code, message);
    }
}
