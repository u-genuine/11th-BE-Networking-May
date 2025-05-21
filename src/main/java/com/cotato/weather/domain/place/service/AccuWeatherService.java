package com.cotato.weather.domain.place.service;

import com.cotato.weather.domain.place.config.AccuWeatherApiConfig;
import com.cotato.weather.domain.place.dto.response.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccuWeatherService {
    private final AccuWeatherApiConfig accuWeatherApiConfig;
    private final RestTemplate restTemplate;
    private final PlaceService placeService;

    //AccuWeather API는 구현못해서 더미 데이터로 대체
    public WeatherResponse getWeather(Long placeId) {
        log.info("서브 API accuWeather 호출");
        //accuweather API 테스트용 더미 데이터
        CurrentResponse currentResponse = new CurrentResponse();
        List<DailyResponse> dailyResponseList = List.of(new DailyResponse());
        List<HourlyResponse> hourlyResponseList = List.of(new HourlyResponse());
        AirPollutionResponse airPollutionResponse = new AirPollutionResponse();

        return makeResponse(currentResponse, dailyResponseList, hourlyResponseList, airPollutionResponse);
    }

    private WeatherResponse makeResponse(CurrentResponse currentResponse, List<DailyResponse> dailyResponseList, List<HourlyResponse> hourlyResponseList, AirPollutionResponse airPollutionResponse) {
        currentResponse.setAirPollutionResponse(airPollutionResponse);

        return WeatherResponse.builder()
                .currentResponse(currentResponse)
                .dailyResponse(dailyResponseList)
                .hourlyResponse(hourlyResponseList)
                .build();
    }

}
