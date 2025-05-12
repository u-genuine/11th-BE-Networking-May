package com.cotato.weather.domain.place.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Uvi {
    LOW("낮음",0, 3),
    MODERATE("보통", 3, 6),

    HIGH("높음", 6, 8),
    VERY_HIGH("매우높음",8, 11),
    EXTREME("위험", 11, Double.MAX_EXPONENT);

    private final String level;
    private final double min;
    private final double max;

    public static String getLevelByUvi(double uvi) {
        for (Uvi uviEnum : Uvi.values()) {
            if (uvi >= uviEnum.min && uvi <= uviEnum.max) {
                return uviEnum.level;
            }
        }
        throw new IllegalArgumentException("Invalid UVI value: " + uvi);
    }

}
