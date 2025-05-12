package com.cotato.weather.domain.place.enums;

public enum DailyAttribute {
    DATE("dt"),
    DAY_TEMPERATURE("day"),
    EVE_TEMPERATURE("eve"),
    HUMIDITY("humidity"),
    WEATHER_DESCRIPTION("description");

    private final String jsonPath;

    DailyAttribute(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    public String getJsonPath() {
        return jsonPath;
    }
}
