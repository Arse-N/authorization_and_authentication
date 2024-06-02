package com.stellarlabs.authentication_and_authorization_service.dto.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogoutResponseDto {
    private String token;
}
