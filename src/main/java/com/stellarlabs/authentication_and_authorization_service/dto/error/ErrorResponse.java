package com.stellarlabs.authentication_and_authorization_service.dto.error;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {

    private String microserviceName;

    private int httpStatus;

    private String message;

    private String path;

    private String errorType;

    private String timestamp;
}
