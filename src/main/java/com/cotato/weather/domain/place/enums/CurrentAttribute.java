package com.cotato.weather.domain.place.enums;

public enum CurrentAttribute {
    //API로 받아온 JSON 데이터중 필요한 값만 필터링하기 위한 enum
    DATE("dt"),
    SUNRISE("sunrise"),
    TEMPERATURE("temp"),
    FEELS_LIKE("feels_like"),
    HUMIDITY("humidity"),
    WIND_DIRECTION("wind_deg"),
    UVI("uvi"),
    WEATHER_DESCRIPTION("description"),
    WIND_SPEED("wind_speed");

    private final String jsonPath;

    CurrentAttribute(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    public String getJsonPath() {
        return jsonPath;
    }

}
