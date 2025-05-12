package com.cotato.weather.domain.place.service;

import com.cotato.weather.domain.place.config.OpenWeatherMapApiConfig;
import com.cotato.weather.domain.place.dto.response.*;
import com.cotato.weather.domain.place.entity.SavedPlace;
import com.cotato.weather.domain.place.enums.AirPollutionAttribute;
import com.cotato.weather.domain.place.enums.CurrentAttribute;
import com.cotato.weather.domain.place.enums.DailyAttribute;
import com.cotato.weather.domain.place.enums.HourlyAttribute;
import com.cotato.weather.domain.place.util.WeatherUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final PlaceService placeService;
    private final OpenWeatherMapApiConfig openWeatherMapApiConfig;
    private final RestTemplate restTemplate;

    public WeatherResponse getWeather(Long placeId) {
        SavedPlace savedPlace = placeService.getSavedPlace(placeId);

        String weatherUrl = buildUrl(openWeatherMapApiConfig.getWeatherUrl(),
                openWeatherMapApiConfig.getApiKey(), savedPlace.getX(), savedPlace.getY());
        String weatherResponse = getResponse(weatherUrl);

        String airPollutionUrl = buildUrl(openWeatherMapApiConfig.getAirPollutionUrl(),
                openWeatherMapApiConfig.getApiKey(), savedPlace.getX(), savedPlace.getY());
        String airPollutionResponse = getResponse(airPollutionUrl);

        CurrentResponse currentResponse = getCurrent(weatherResponse);
        List<DailyResponse> dailyResponse = getDaily(weatherResponse);
        List<HourlyResponse> hourlyResponse = getHourly(weatherResponse);
        AirPollutionResponse airPollutionResponseObj = getAirPollution(airPollutionResponse);

        return makeResponse(currentResponse, dailyResponse, hourlyResponse, airPollutionResponseObj);
    }

    private WeatherResponse makeResponse(CurrentResponse currentResponse, List<DailyResponse> dailyResponseList, List<HourlyResponse> hourlyResponseList, AirPollutionResponse airPollutionResponse) {
        currentResponse.setAirPollutionResponse(airPollutionResponse);

        return WeatherResponse.builder()
                .currentResponse(currentResponse)
                .dailyResponse(dailyResponseList)
                .hourlyResponse(hourlyResponseList)
                .build();
    }

    private CurrentResponse getCurrent(String weatherResponse) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(weatherResponse);
            JsonNode current = root.get("current");
            JsonNode weatherArray = current.get("weather").get(0);

            return CurrentResponse.builder()
                    .date(WeatherUtil.epochSecondsToDate(current.get(CurrentAttribute.DATE.getJsonPath()).asLong()))
                    .sunset(WeatherUtil.epochSecondsToTime(current.get(CurrentAttribute.SUNRISE.getJsonPath()).asLong()))
                    .temperature(WeatherUtil.kelvinToCelsius(current.get(CurrentAttribute.TEMPERATURE.getJsonPath()).asDouble()))
                    .feelsLike(WeatherUtil.kelvinToCelsius(current.get(CurrentAttribute.FEELS_LIKE.getJsonPath()).asDouble()))
                    .humidity(current.get(CurrentAttribute.HUMIDITY.getJsonPath()).asDouble())
                    .windDirection(WeatherUtil.getWindDirection(current.get(CurrentAttribute.WIND_DIRECTION.getJsonPath()).asDouble()))
                    .windSpeed(current.get(CurrentAttribute.WIND_SPEED.getJsonPath()).asDouble())
                    .uviLevel(WeatherUtil.getUviLevel(current.get(CurrentAttribute.UVI.getJsonPath()).asDouble()))
                    .weatherDescription(weatherArray.get(CurrentAttribute.WEATHER_DESCRIPTION.getJsonPath()).asText())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse weather response", e);
        }
    }

    private List<HourlyResponse> getHourly(String weatherResponse) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(weatherResponse);
            JsonNode hourly = root.get("hourly");

            //hourly는 총 12시간 가져오기
            int hourlyCount = Math.min(hourly.size(), 12);
            List<HourlyResponse> hourlyResponseList = new ArrayList<>();

            for (int i = 0; i < hourlyCount; i++) {
                JsonNode hourlyData = hourly.get(i);
                JsonNode weatherArray = hourlyData.get("weather").get(0);

                HourlyResponse hourlyResponse = HourlyResponse.builder()
                        .date(WeatherUtil.epochSecondsToTime(hourlyData.get(HourlyAttribute.DATE_TIME.getJsonPath()).asLong()))
                        .temperature(WeatherUtil.kelvinToCelsius(hourlyData.get(HourlyAttribute.TEMPERATURE.getJsonPath()).asDouble()))
                        .weatherDescription(weatherArray.get(HourlyAttribute.WEATHER_DESCRIPTION.getJsonPath()).asText())
                        .build();

                hourlyResponseList.add(hourlyResponse);
            }
            return hourlyResponseList;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse weather response", e);
        }
    }

    private List<DailyResponse> getDaily(String weatherResponse) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(weatherResponse);
            JsonNode daily = root.get("daily");

            //daily는 총 5일 가져오기
            int dailyCount = Math.min(daily.size(), 5);
            List<DailyResponse> dailyResponseList = new ArrayList<>();

            for (int i = 0; i < dailyCount; i++) {
                JsonNode dailyData = daily.get(i);
                JsonNode weatherArray = dailyData.get("weather").get(0);
                JsonNode temp = dailyData.get("temp");

                DailyResponse dailyResponse = DailyResponse.builder()
                        .date(WeatherUtil.epochSecondsToDate(dailyData.get(DailyAttribute.DATE.getJsonPath()).asLong()))
                        .humidity(dailyData.get(DailyAttribute.HUMIDITY.getJsonPath()).asDouble())
                        .dayTemperature(WeatherUtil.kelvinToCelsius(temp.get(DailyAttribute.DAY_TEMPERATURE.getJsonPath()).asDouble()))
                        .eveTemperature(WeatherUtil.kelvinToCelsius(temp.get(DailyAttribute.EVE_TEMPERATURE.getJsonPath()).asDouble()))
                        .weatherDescription(weatherArray.get(DailyAttribute.WEATHER_DESCRIPTION.getJsonPath()).asText())
                        .build();

                dailyResponseList.add(dailyResponse);
            }
            return dailyResponseList;

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse weather response", e);
        }

    }

    private AirPollutionResponse getAirPollution(String airPollutionResponse) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(airPollutionResponse);
            JsonNode airPollution = root.get("list").get(0);
            JsonNode components = airPollution.get("components");

            return AirPollutionResponse.builder()
                    .pm2_5Level(WeatherUtil.getAirPollutionLevelByDust(components.get(AirPollutionAttribute.PM2_5.getJsonKey()).asDouble()))
                    .pm10Level(WeatherUtil.getAirPollutionLevelByDust(components.get(AirPollutionAttribute.PM10.getJsonKey()).asDouble()))
                    .build();

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse air pollution response", e);
        }
    }

//    public WeatherResponse testWeather() {
//        String url = buildUrl(openWeatherMapApiConfig.getWeatherUrl(), openWeatherMapApiConfig.getApiKey(), 37.5665, 126.978);
//
//        String weatherResponse = getResponse(url);
//
//        String airPollutionUrl = buildUrl(openWeatherMapApiConfig.getAirPollutionUrl(),
//                openWeatherMapApiConfig.getApiKey(), 37.5665, 126.978);
//
//        String airPollutionResponse = getResponse(airPollutionUrl);
//
//        CurrentResponse currentResponse = getCurrent(weatherResponse);
//        List<DailyResponse> dailyResponse = getDaily(weatherResponse);
//        List<HourlyResponse> hourlyResponse = getHourly(weatherResponse);
//        AirPollutionResponse airPollutionResponseObj = getAirPollution(airPollutionResponse);
//
//        return makeResponse(currentResponse, dailyResponse, hourlyResponse, airPollutionResponseObj);
//
//    }

    private String getResponse(String url) {
        return restTemplate.getForObject(url, String.class);
    }

    private String buildUrl(String url, String appId, Double lat, Double lon) {
        return UriComponentsBuilder.fromUriString(url)
                .queryParam("appid", appId)
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .toUriString();
    }


}
