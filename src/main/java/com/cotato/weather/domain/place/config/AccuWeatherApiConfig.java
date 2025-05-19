package com.cotato.weather.domain.place.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "api-key.accu-weather")
public class AccuWeatherApiConfig {
    private String apiKey;
    private String currentUrl;
    private String hourlyUrl;
    private String dailyUrl;
    private String locationUrl;

}
