package com.cotato.weather.domain.place.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class WeatherDetailRequest {
    Double x;
    Double y;
}
