package com.cotato.weather.domain.place.request;

import com.cotato.weather.domain.place.entity.SavedPlace;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SavedPlaceCreateRequest {
    private String placeName;
    private Double x;
    private Double y;


    public SavedPlace toEntity() {
        return SavedPlace.builder()
                .placeName(placeName)
                .isPinned("N")
                .x(x)
                .y(y)
                .build();
    }
}
