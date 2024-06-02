package com.stellarlabs.authentication_and_authorization_service.dto.error;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Message {

    private int statusCode;

    private String message;

    private String status;

}
