package com.cotato.weather.domain.place.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor
@Builder
@Getter
public class AirPollutionResponse {
    private String pm10Level; //미세먼지
    private String pm2_5Level; //초미세먼지
}
