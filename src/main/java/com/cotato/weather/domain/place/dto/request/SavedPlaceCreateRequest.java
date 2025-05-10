package com.cotato.weather.domain.place.dto.request;

import com.cotato.weather.domain.place.entity.SavedPlace;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SavedPlaceCreateRequest {
    private String placeName;
    private Double x;
    private Double y;


}
