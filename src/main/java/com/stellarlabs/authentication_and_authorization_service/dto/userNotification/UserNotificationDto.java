package com.stellarlabs.authentication_and_authorization_service.dto.userNotification;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNotificationDto {

    private String accountUuid;

    private String businessUuid;

    private String websiteUuid;

    private String message;

    private LocalDateTime date;

}
