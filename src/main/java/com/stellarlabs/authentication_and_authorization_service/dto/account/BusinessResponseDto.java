package com.stellarlabs.authentication_and_authorization_service.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BusinessResponseDto {

    private String uuid;

    private String name;

    private String email;

    private String phone;

    private String country;

    private String address;

    private String city;

    private String zoneID;
}
