package com.stellarlabs.authentication_and_authorization_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailVerificationDto {

    String token;

    private String businessUuid;

}
