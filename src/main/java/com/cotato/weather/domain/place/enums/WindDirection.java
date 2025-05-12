package com.cotato.weather.domain.place.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WindDirection {
    NORTH(0, 45,"북"),
    NORTHEAST(45, 90,"북동"),
    EAST(90, 135,"동"),
    SOUTHEAST(135, 180,"남동"),
    SOUTH(180, 225,"남"),
    SOUTHWEST(225, 270,"남서"),
    WEST(270, 315,"서"),
    NORTHWEST(315, 360,"북서");

    private final double min;
    private final double max;
    private final String description;

    public static String getDescriptionFromDegree(double windDeg) {
        for (WindDirection direction : WindDirection.values()) {
            if (windDeg >= direction.min && windDeg < direction.max) {
                return direction.getDescription();
            }
        }

        throw new IllegalArgumentException("Invalid degree: " + windDeg);
    }
}
