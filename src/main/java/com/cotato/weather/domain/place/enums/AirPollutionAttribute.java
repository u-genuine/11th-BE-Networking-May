package com.cotato.weather.domain.place.enums;

public enum AirPollutionAttribute {
    PM10("pm10"),
    PM2_5("pm2_5");

    private final String jsonPath;

    AirPollutionAttribute(String jsonKey) {
        this.jsonPath = jsonKey;
    }

    public String getJsonKey() {
        return jsonPath;
    }
}
