package com.stellarlabs.authentication_and_authorization_service.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {

    private String uuid;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String zipCode;

    private String birthday;

    private String street1;

    private String street2;

    private String country;

    private String city;

    private String state;

    private String fullAddress;
}
