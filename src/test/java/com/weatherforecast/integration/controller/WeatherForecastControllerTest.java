package com.weatherforecast.integration.controller;

import com.weatherforecast.controller.WeatherForecastController;
import com.weatherforecast.model.request.WeatherForecastRequest;
import com.weatherforecast.model.response.WeatherForecastResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith (SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = WeatherForecastApplication.class)
public class WeatherForecastControllerTest {

    @Autowired
    WeatherForecastController weatherForecastController;

    private ResponseEntity<WeatherForecastResponse> weatherForecastResponse;

    @Test
    public void getWeatherForecastDataWithValidZip() {
        weatherForecastResponse = weatherForecastController.getWeatherForecastData (WeatherForecastRequest.builder ()
                .zipCode (85383)
                .build ());
        assertTrue (null != weatherForecastResponse);
    }

    @Test
    public void getWeatherForecastDataWithValidZipAndCountry() {
        weatherForecastResponse = weatherForecastController.getWeatherForecastData (WeatherForecastRequest.builder ()
                .zipCode (85383)
                .country ("US")
                .build ());
        assertTrue (null != weatherForecastResponse);
    }

   @Test(expected = HttpClientErrorException.class)
   public void getWeatherForecastDataWithInvalidZip() {
        weatherForecastResponse = weatherForecastController.getWeatherForecastData (WeatherForecastRequest.builder ()
                .zipCode (98)
                .build ());
    }

    @Test(expected = HttpClientErrorException.class)
    public void getWeatherForecastDataWithNoInput() {
        weatherForecastResponse = weatherForecastController.getWeatherForecastData (WeatherForecastRequest.builder ()
                .build ());
    }

    @Test(expected = HttpClientErrorException.class)
    public void getWeatherForecastDataWithValidZipAndInvalidCountry() {
        weatherForecastResponse = weatherForecastController.getWeatherForecastData (WeatherForecastRequest.builder ()
                .zipCode (85383)
                .country ("NZ")
                .build ());
    }

}
