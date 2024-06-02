package com.stellarlabs.authentication_and_authorization_service.service.impl;

import com.stellarlabs.authentication_and_authorization_service.dto.error.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
class ExceptionServiceImplTest {

    @Value("${error-service.path}")
    private String baseurl;

    @Mock
    RestTemplate restTemplate;

    ErrorResponse errorResponse = ErrorResponse.builder()
            .microserviceName("A&A")
            .path("aasassssasssssasas")
            .errorType("NOT_FOUND")
            .message("asssadsadsa")
            .httpStatus(404)
            .build();

    @Test
    void sendException() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<ErrorResponse> entity = new HttpEntity<>(errorResponse,headers);
        when(restTemplate.getForEntity(baseurl + "/", ErrorResponse.class))
                .thenReturn(new ResponseEntity(entity, HttpStatus.OK));
    }
}