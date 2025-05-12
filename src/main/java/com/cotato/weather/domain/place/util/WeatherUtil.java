package com.cotato.weather.domain.place.util;

import com.cotato.weather.domain.place.enums.AirPollution;
import com.cotato.weather.domain.place.enums.Uvi;
import com.cotato.weather.domain.place.enums.WindDirection;

import java.time.Instant;
import java.time.ZoneId;

public class WeatherUtil {

    // Convert epoch seconds to date string in "yyyy-MM-dd" format
    public static String epochSecondsToDate(Long dt) {
        return Instant.ofEpochSecond(dt)
                .atZone(ZoneId.of("Asia/Seoul"))
                .toLocalDate()
                .toString();
    }

    // Convert epoch seconds to time string in "HH:mm:ss" format
    public static String epochSecondsToTime(Long dt) {
        return Instant.ofEpochSecond(dt)
                .atZone(ZoneId.of("Asia/Seoul"))
                .toLocalDateTime()
                .toString();
    }

    // Kelvin to Celsius conversion
    public static double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }

    // degree로부터 풍향 계산
    public static String getWindDirection(double windDeg) {
        return WindDirection.getDescriptionFromDegree(windDeg);
    }


    //uvi 단계 계산
    public static String getUviLevel(double uvi) {
        return Uvi.getLevelByUvi(uvi);
    }

    //미세먼지 지수단계 계산
    public static String getAirPollutionLevelByDust(double dust) {
        return AirPollution.getLevelByValue(dust);
    }







}
