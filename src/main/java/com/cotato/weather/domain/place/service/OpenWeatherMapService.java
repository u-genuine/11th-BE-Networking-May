package com.cotato.weather.domain.place.service;

import com.cotato.weather.domain.exception.BadParameterException;
import com.cotato.weather.domain.exception.ExternalApiServerException;
import com.cotato.weather.domain.exception.JsonParsingException;
import com.cotato.weather.domain.place.config.OpenWeatherMapApiConfig;
import com.cotato.weather.domain.place.dto.response.*;
import com.cotato.weather.domain.place.dto.vo.ApiStatus;
import com.cotato.weather.domain.place.entity.SavedPlace;
import com.cotato.weather.domain.place.enums.AirPollutionAttribute;
import com.cotato.weather.domain.place.enums.CurrentAttribute;
import com.cotato.weather.domain.place.enums.DailyAttribute;
import com.cotato.weather.domain.place.enums.HourlyAttribute;
import com.cotato.weather.domain.place.util.WeatherUtil;
import com.cotato.weather.global.exception.AppException;
import com.cotato.weather.global.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OpenWeatherMapService {
    private final PlaceService placeService;
    private final OpenWeatherMapApiConfig openWeatherMapApiConfig;
    private final RestTemplate restTemplate;
    private final WeatherApiFailService weatherApiFailService;

    public WeatherResponse getWeather(Long placeId) {
        SavedPlace savedPlace = placeService.getSavedPlace(placeId);

        String weatherUrl = buildUrl(openWeatherMapApiConfig.getWeatherUrl(),
                openWeatherMapApiConfig.getApiKey(), savedPlace.getY(), savedPlace.getX());

        String weatherResponse = null;
        try {
            weatherResponse = getResponse(weatherUrl);
        } catch (ExternalApiServerException e) {
            //외부 API 서버 에러 발생시(cod 500번대, 400, 429번)
            //ERROR 로그 저장 후 서브 API로 Retry
            return weatherApiFailService.failExternalApi(e, placeId);
        } catch (BadParameterException e) {
            //위도, 경도값을 찾을 수 없는 경우 404
            //잘못된 위도, 경도값을 입력한 경우 400
            throw new AppException(e.getErrorCode());
        }

        String airPollutionUrl = buildUrl(openWeatherMapApiConfig.getAirPollutionUrl(),
                openWeatherMapApiConfig.getApiKey(), savedPlace.getY(), savedPlace.getX());

        String airPollutionApiResponse = null;
        try {
            airPollutionApiResponse = getResponse(airPollutionUrl);
        } catch (ExternalApiServerException e) {
            return weatherApiFailService.failExternalApi(e, placeId);
        }

        CurrentResponse currentResponse;
        List<DailyResponse> dailyResponse;
        List<HourlyResponse> hourlyResponse;
        AirPollutionResponse airPollutionResponse;

        try {
            currentResponse = getCurrent(weatherResponse);
            dailyResponse = getDaily(weatherResponse);
            hourlyResponse = getHourly(weatherResponse);
            airPollutionResponse = getAirPollution(airPollutionApiResponse);
        } catch (JsonParsingException e) {
            return weatherApiFailService.failJsonParsing(e, placeId);
        }

        return makeResponse(currentResponse, dailyResponse, hourlyResponse, airPollutionResponse);
    }

    public ApiStatus checkApiStatus() {
        Long testPlaceId = 2L; // 테스트용으로 사용할 장소 ID

        SavedPlace savedPlace = placeService.getSavedPlace(testPlaceId);

        String weatherUrl = buildUrl(openWeatherMapApiConfig.getWeatherUrl(),
                openWeatherMapApiConfig.getApiKey(), savedPlace.getY(), savedPlace.getX());

        String weatherResponse = null;

        try {
            weatherResponse = getResponse(weatherUrl);
        } catch (ExternalApiServerException e) {
            // 외부 API 서버 에러 발생시(cod 500번대, 400, 429번)
            // ERROR 로그 저장 후 서브 API로 Retry
            return ApiStatus.builder()
                    .status("ERROR")
                    .message(e.getErrorCode().getMessage())
                    .code(e.getErrorCode().getCode())
                    .build();
        }

        String airPollutionUrl = buildUrl(openWeatherMapApiConfig.getAirPollutionUrl(),
                openWeatherMapApiConfig.getApiKey(), savedPlace.getY(), savedPlace.getX());

        String airPollutionResponse = null;
        try {
            airPollutionResponse = getResponse(airPollutionUrl);
        } catch (ExternalApiServerException e) {
            return ApiStatus.builder()
                    .status("ERROR")
                    .message(e.getErrorCode().getMessage())
                    .code(e.getErrorCode().getCode())
                    .build();
        }

        //Json Parsing 예외 처리
        try {
            getCurrent(weatherResponse);
            getDaily(weatherResponse);
            getHourly(weatherResponse);
            getAirPollution(airPollutionResponse);
        } catch (JsonParsingException e) {
            return ApiStatus.builder()
                    .status("ERROR")
                    .message(e.getErrorCode().getMessage())
                    .code(e.getErrorCode().getCode())
                    .build();
        }

        return ApiStatus.builder()
                .status("OK")
                .message("API is available")
                .code("200")
                .build();
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
            System.out.println("getCurrent Parsing Error");
            throw new JsonParsingException(ErrorCode.PARSING_ERROR);
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
            System.out.println("getHourly Parsing Error");
            throw new JsonParsingException(ErrorCode.PARSING_ERROR);
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
            System.out.println("getDaily Parsing Error");
            throw new JsonParsingException(ErrorCode.PARSING_ERROR);
        }

    }

    private AirPollutionResponse getAirPollution(String airPollutionResponse) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(airPollutionResponse);
            JsonNode airPollution = root.get("list").get(0);
            JsonNode components = airPollution.get("components");
            System.out.println(components.toString());


            System.out.println("getAirPollution Parsing Success");

            return AirPollutionResponse.builder()
                    .pm2_5Level(WeatherUtil.getAirPollutionLevelByDust(components.get(AirPollutionAttribute.PM2_5.getJsonKey()).asDouble()))
                    .pm10Level(WeatherUtil.getAirPollutionLevelByDust(components.get(AirPollutionAttribute.PM10.getJsonKey()).asDouble()))
                    .build();

        } catch (Exception e) {
            System.out.println("getAirPollution Parsing Error");
            throw new JsonParsingException(ErrorCode.PARSING_ERROR);
        }
    }

    public WeatherResponse testWeather(Long placeId) {
        SavedPlace savedPlace = placeService.getSavedPlace(placeId);

        String weatherUrl = buildUrl(openWeatherMapApiConfig.getWeatherUrl(),
                openWeatherMapApiConfig.getApiKey(), savedPlace.getY(), savedPlace.getX());

        String weatherResponse = null;
        try {
            weatherResponse = getResponse(weatherUrl);
        } catch (ExternalApiServerException e) {
            //외부 API 서버 에러 발생시(cod 500번대, 400, 429번)
            //ERROR 로그 저장 후 서브 API로 Retry
            return weatherApiFailService.failExternalApi(e, placeId);
        } catch (BadParameterException e) {
            //위도, 경도값을 찾을 수 없는 경우 404
            //잘못된 위도, 경도값을 입력한 경우 400
            throw new AppException(e.getErrorCode());
        }

        String airPollutionUrl = buildUrl(openWeatherMapApiConfig.getAirPollutionUrl(),
                openWeatherMapApiConfig.getApiKey(), savedPlace.getY(), savedPlace.getX());

        String airPollutionApiResponse = null;
        try {
            airPollutionApiResponse = getResponse(airPollutionUrl);
        } catch (ExternalApiServerException e) {
            return weatherApiFailService.failExternalApi(e, placeId);
        }

        CurrentResponse currentResponse;
        List<DailyResponse> dailyResponse;
        List<HourlyResponse> hourlyResponse;
        AirPollutionResponse airPollutionResponse;

        try {
            currentResponse = getCurrent(weatherResponse);
            dailyResponse = getDaily(weatherResponse);
            hourlyResponse = getHourly(weatherResponse);
            airPollutionResponse = getAirPollution(airPollutionApiResponse);
        } catch (JsonParsingException e) {
            return weatherApiFailService.failJsonParsing(e, placeId);
        }

        return makeResponse(currentResponse, dailyResponse, hourlyResponse, airPollutionResponse);
    }

    private String getResponse(String url) {
        try {
            return restTemplate.getForObject(url, String.class);
        } catch (RestClientResponseException e) {
            String responseBody = e.getResponseBodyAsString();

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                int statusCode = jsonNode.get("cod").asInt();

                if(statusCode == 400) {
                    throw new BadParameterException(ErrorCode.EXTERNAL_API_SERVER_BAD_REQUEST);
                } else if(statusCode == 401) {
                    throw new ExternalApiServerException(ErrorCode.EXTERNAL_API_SERVER_UNAUTHORIZED);
                } else if (statusCode == 404) {
                    throw new BadParameterException(ErrorCode.EXETRNAL_API_SERVER_NOT_FOUND_LAT_LON);
                } else if (statusCode == 429) {
                    throw new ExternalApiServerException(ErrorCode.EXTERNAL_API_SERVER_EXCEED_RATE_LIMIT);
                } else { //500번대 에러
                    throw new ExternalApiServerException(ErrorCode.EXTERNAL_API_SERVER_ERROR);
                }

            } catch (JsonProcessingException ex) {
                throw new JsonParsingException(ErrorCode.PARSING_ERROR);
            }

        }
    }

    private String buildUrl(String url, String appId, Double lat, Double lon) {
        return UriComponentsBuilder.fromUriString(url)
                .queryParam("appid", appId)
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .toUriString();
    }


}
