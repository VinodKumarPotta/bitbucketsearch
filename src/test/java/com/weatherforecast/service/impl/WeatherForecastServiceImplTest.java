package com.weatherforecast.service.impl;

import com.weatherforecast.model.request.WeatherForecastRequest;
import com.weatherforecast.model.response.WeatherForecastResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = WeatherForecastApplication.class)
public class WeatherForecastServiceImplTest {

    @Autowired
    WeatherForecastServiceImpl weatherForecastServiceImpl;

    private ResponseEntity<WeatherForecastResponse> weatherForecastResponse;

    @Test
    public void getWeatherForecastDataWithZip() {
        weatherForecastResponse = weatherForecastServiceImpl.getWeatherForecastData (WeatherForecastRequest.builder ()
                .zipCode (85383)
                .build ());
        assertTrue (null != weatherForecastResponse);
    }

    @Test
    public void getWeatherForecastDataWithZipAndCountry() {
        weatherForecastResponse = weatherForecastServiceImpl.getWeatherForecastData (WeatherForecastRequest.builder ()
                .zipCode (85383)
                .country ("US")
                .build ());
        assertTrue (null != weatherForecastResponse);
    }
    @Test(expected = HttpClientErrorException.class)
    public void getWeatherForecastDataWithInvalidZip() {
        weatherForecastResponse = weatherForecastServiceImpl.getWeatherForecastData (WeatherForecastRequest.builder ()
                .zipCode (98)
                .build ());
    }

    @Test(expected = HttpClientErrorException.class)
    public void getWeatherForecastDataWithNoInput() {
        weatherForecastResponse = weatherForecastServiceImpl.getWeatherForecastData (WeatherForecastRequest.builder ()
                .build ());
    }

    @Test(expected = MethodArgumentTypeMismatchException.class)
    public void getWeatherForecastDataWithValidZipAndInvalidCountry() {
        weatherForecastResponse = weatherForecastServiceImpl.getWeatherForecastData (WeatherForecastRequest.builder ()
                .zipCode (85383)
                .country (null)
                .build ());
    }

}