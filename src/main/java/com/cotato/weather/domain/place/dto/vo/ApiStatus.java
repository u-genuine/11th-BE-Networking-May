package com.cotato.weather.domain.place.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ApiStatus {
    private String status;
    private String message;
    private String code;
}
