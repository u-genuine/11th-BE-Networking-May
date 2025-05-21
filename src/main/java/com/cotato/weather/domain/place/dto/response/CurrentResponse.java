package com.cotato.weather.domain.place.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrentResponse {
    private String date;
    private String weatherDescription; //날씨 설명
    private double temperature; //온도
    private double feelsLike; //체감온도
    private double humidity; //습도
    private String windDirection; //풍향
    private String sunset; //일출
    private String uviLevel;//자외선
    private double windSpeed; //풍속
    private AirPollutionResponse airPollution; //미세먼지


    public void setAirPollutionResponse(AirPollutionResponse airPollutionResponse) {
        this.airPollution = airPollutionResponse;
    }
}
