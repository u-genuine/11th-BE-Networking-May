package com.cotato.weather.domain.place.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class DailyResponse {
    private String date;
    private String weatherDescription;
    private double dayTemperature;
    private double eveTemperature;
    private double humidity;



}
