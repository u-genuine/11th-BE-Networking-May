package com.cotato.weather.domain.place.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component @Getter @Setter
@ConfigurationProperties(prefix = "api-key.open-weather-map")
public class OpenWeatherMapApiConfig {
    private String apiKey;
    private String baseUrl;


}
