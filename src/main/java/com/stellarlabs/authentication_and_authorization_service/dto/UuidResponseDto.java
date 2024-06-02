package com.stellarlabs.authentication_and_authorization_service.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UuidResponseDto {

    String uuid;

    @Override
    public String toString() {
        return "UuidResponseDto{" +
                "uuid='" + uuid + '\'' +
                '}';
    }
}
