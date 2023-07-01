package com.weatherforecast.juint.mockito.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherforecast.exception.ClientExceptions;
import com.weatherforecast.service.impl.WeatherForecastServiceImpl;
import com.weatherforecast.model.helper.WeatherForecastHelperMain;
import com.weatherforecast.model.request.WeatherForecastRequest;
import com.weatherforecast.model.response.WeatherForecastResponse;
import com.weatherforecast.model.response.helper.WeatherForecastDetails;
import com.weatherforecast.model.response.helper.WeatherForecastStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;

@ExtendWith (MockitoExtension.class)
public class WeatherForecastServiceImplTest {

    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    @Spy
    WeatherForecastServiceImpl weatherForecastServiceImpl;

    private ResponseEntity<WeatherForecastResponse> weatherForecastResponseResponseEntity;

    private WeatherForecastRequest validWeatherForecastRequest;

    private WeatherForecastRequest invalidWeatherForecastRequest;


    @BeforeEach
    void setUp(){

        ReflectionTestUtils.setField(weatherForecastServiceImpl, "targetURL", "http://api.openweathermap.org/data/2.5/forecast");
        ReflectionTestUtils.setField(weatherForecastServiceImpl, "apiKey", "0654521babab26b6445b219c83a63fd5");

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
    public void getWeatherForecastDataWithValidZip() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File("src/test/resources/WeatherForecastHelperMain.json");
        WeatherForecastHelperMain weatherForecastHelperMain = new ObjectMapper().readValue (new File("src/test/resources/WeatherForecastHelperMain.json"), WeatherForecastHelperMain.class);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        Mockito.when (restTemplate.exchange ("http://api.openweathermap.org/data/2.5/forecast?zip=85383,US&appid=0654521babab26b6445b219c83a63fd5&units=metric",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<WeatherForecastHelperMain> ( ) {
                }))
                .thenReturn (new ResponseEntity<> (weatherForecastHelperMain,HttpStatus.OK));
       Assertions.assertNotNull (weatherForecastServiceImpl.getWeatherForecastData (validWeatherForecastRequest));
    }

    @Test
    public void getWeatherForecastDataWithValidZipException() throws IOException {
        ReflectionTestUtils.setField(weatherForecastServiceImpl, "apiKey", "test");
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File("src/test/resources/WeatherForecastHelperMain.json");
        WeatherForecastHelperMain weatherForecastHelperMain = new ObjectMapper().readValue (new File("src/test/resources/WeatherForecastHelperMain.json"), WeatherForecastHelperMain.class);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<Object> requestEntity = new HttpEntity<>(headers);
        Mockito.when (restTemplate.exchange ("http://api.openweathermap.org/data/2.5/forecast?zip=85383,US&appid=test&units=metric",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<WeatherForecastHelperMain> ( ) {
                }))
                //.thenThrow ( new ClientExceptions ("Client Exception","Unable to fetch Weather data"));
        .thenReturn (new ResponseEntity<> (weatherForecastHelperMain,HttpStatus.UNAUTHORIZED));
        Assertions.assertThrows (ClientExceptions.class, () ->{
            weatherForecastServiceImpl.getWeatherForecastData (validWeatherForecastRequest);
        });
    }

}