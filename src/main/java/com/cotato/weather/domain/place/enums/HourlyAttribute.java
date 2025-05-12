package com.cotato.weather.domain.place.enums;

public enum HourlyAttribute {
    DATE_TIME("dt"),
    TEMPERATURE("temp"),
    WEATHER_DESCRIPTION("description");
    private final String jsonPath;

    HourlyAttribute(String jsonPath) {
        this.jsonPath = jsonPath;
    }

    public String getJsonPath() {
        return jsonPath;
    }

}
