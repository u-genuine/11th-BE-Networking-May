package com.cotato.weather.domain.place.response;

import com.cotato.weather.domain.place.entity.SavedPlace;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SavedPlaceListResponse {
    private Long id;

    private String placeName;

    private Double x;

    private Double y;

    private String isPinned;

    public static SavedPlaceListResponse from(SavedPlace savedPlace) {
        return SavedPlaceListResponse.builder()
                .id(savedPlace.getId())
                .placeName(savedPlace.getPlaceName())
                .x(savedPlace.getX())
                .y(savedPlace.getY())
                .isPinned(savedPlace.getIsPinned())
                .build();
    }
}
