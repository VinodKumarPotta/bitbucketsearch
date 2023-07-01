package com.weatherforecast.controller;

import com.weatherforecast.service.impl.WeatherForecastServiceImpl;
import com.weatherforecast.model.request.WeatherForecastRequest;
import com.weatherforecast.model.response.WeatherForecastResponse;
import com.weatherforecast.model.response.helper.WeatherForecastDetails;
import com.weatherforecast.model.response.helper.WeatherForecastStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;

@ExtendWith (MockitoExtension.class)
class WeatherForecastControllerTest {

    @Mock
    WeatherForecastServiceImpl weatherForecastService;

    @InjectMocks
    WeatherForecastController weatherForecastController;

    private ResponseEntity<WeatherForecastResponse> weatherForecastResponseResponseEntity;

    private WeatherForecastRequest validWeatherForecastRequest;

    private WeatherForecastRequest invalidWeatherForecastRequest;

    @Before
    void setUp(){

        validWeatherForecastRequest = WeatherForecastRequest.builder ()
                .zipCode (85383)
                .country ("US")
                .build ();

        invalidWeatherForecastRequest = WeatherForecastRequest.builder ()
                .zipCode (99)
                .build ();

        weatherForecastResponseResponseEntity = ResponseEntity.ok(WeatherForecastResponse.builder()
                .country("US")
                .city("Pel")
                .weatherForecastDetails (Arrays.asList (WeatherForecastDetails.builder ()
                        .date(Instant.ofEpochSecond(1622203200).atZone(ZoneId.systemDefault()).toLocalDateTime())
                        .temperature(23.32)
                        .feelsLike(23.32)
                        .temperatureMin(23.32)
                        .temperatureMax(23.32)
                        .humidity(18.0)
                        .windSpeed(0.91)
                        .weatherForecastStatuses (Arrays.asList (WeatherForecastStatus.builder ()
                                .category ("Sky")
                                .detailedDescription ("Sky Clouds").build ()))
                        .build ())).build ());

    }

    @Test
    void getWeatherForecastDataWithValidZip() {
        ResponseEntity<WeatherForecastResponse> weatherForecastResponseResponseEntity=ResponseEntity.ok(WeatherForecastResponse.builder()
                .country("US")
                .city("Pel")
                .weatherForecastDetails (Arrays.asList (WeatherForecastDetails.builder ()
                        .date(Instant.ofEpochSecond(1622203200).atZone(ZoneId.systemDefault()).toLocalDateTime())
                        .temperature(23.32)
                        .feelsLike(23.32)
                        .temperatureMin(23.32)
                        .temperatureMax(23.32)
                        .humidity(18.0)
                        .windSpeed(0.91)
                        .weatherForecastStatuses (Arrays.asList (WeatherForecastStatus.builder ()
                                .category ("Sky")
                                .detailedDescription ("Sky Clouds").build ()))
                        .build ())).build ());
        Mockito.when(weatherForecastService.getWeatherForecastData (validWeatherForecastRequest)).thenReturn (weatherForecastResponseResponseEntity);
        Assert.assertEquals (weatherForecastController.getWeatherForecastData (validWeatherForecastRequest),weatherForecastResponseResponseEntity);
    }

    @Test
    void getWeatherForecastDataWithInvalidZip() {
        ResponseEntity<WeatherForecastResponse> weatherForecastResponseResponseEntity=ResponseEntity.ok(WeatherForecastResponse.builder()
                .country("US")
                .city("Pel")
                .weatherForecastDetails (Arrays.asList (WeatherForecastDetails.builder ()
                        .date(Instant.ofEpochSecond(1622203200).atZone(ZoneId.systemDefault()).toLocalDateTime())
                        .temperature(23.32)
                        .feelsLike(23.32)
                        .temperatureMin(23.32)
                        .temperatureMax(23.32)
                        .humidity(18.0)
                        .windSpeed(0.91)
                        .weatherForecastStatuses (Arrays.asList (WeatherForecastStatus.builder ()
                                .category ("Sky")
                                .detailedDescription ("Sky Clouds").build ()))
                        .build ())).build ());

        Assertions.assertThrows (HttpClientErrorException.class, () -> {
            Mockito.when(weatherForecastService.getWeatherForecastData (invalidWeatherForecastRequest)).thenThrow (HttpClientErrorException.class);
            weatherForecastController.getWeatherForecastData (invalidWeatherForecastRequest);
        });
    }

}