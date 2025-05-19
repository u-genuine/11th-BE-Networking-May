package com.cotato.weather.domain.place.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AirPollution {
    GOOD("좋음", 0.0, 30.0),
    NORMAL("보통", 30.0, 80.0),
    BAD("나쁨", 80.0, 150.0),
    VERY_BAD("매우 나쁨", 150.0, 999.0);

    private final String level;
    private final double min;
    private final double max;

    public static String getLevelByValue(double dust) {
        for (AirPollution airPollution : AirPollution.values()) {
            if (dust >= airPollution.getMin() && dust < airPollution.getMax()) {
                return airPollution.level;
            }
        }

        throw new IllegalArgumentException("Invalid 미세먼지 농도 " + dust);
    }
}
