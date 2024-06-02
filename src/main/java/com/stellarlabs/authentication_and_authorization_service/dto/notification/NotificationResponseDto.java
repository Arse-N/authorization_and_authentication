package com.stellarlabs.authentication_and_authorization_service.dto.notification;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponseDto {

    private String serviceName = "Authorization";

    private String firstName;

    private  String lastName;

    private String email;

    private String subject;

    private int code;

    private String link;

}
