package com.cotato.weather.domain.place.dto.vo;

import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiLog {

    private String status;

    private String code;

    private String message;

    private LocalDateTime time;



}
