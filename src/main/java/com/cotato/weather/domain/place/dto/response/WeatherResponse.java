package com.cotato.weather.domain.place.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
public class WeatherResponse {
    private CurrentResponse currentResponse;
    private List<DailyResponse> dailyResponse;
    private List<HourlyResponse> hourlyResponse;


}
