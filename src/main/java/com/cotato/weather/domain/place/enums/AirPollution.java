package com.cotato.weather.domain.place.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AirPollution {
    GOOD("좋음", 0,30),
    NORMAL("보통", 31,80),
    BAD("나쁨", 81,150),
    VERY_BAD("매우 나쁨", 151, 999);

    private final String level;
    private final int min;
    private final int max;

    public static String getLevelByValue(double dust) {
        for (AirPollution airPollution : AirPollution.values()) {
            if (dust >= airPollution.getMin() && dust <= airPollution.getMax()) {
                return airPollution.level;
            }
        }

        throw new IllegalArgumentException("Invalid 미세먼지 농도 " + dust);
    }
}
