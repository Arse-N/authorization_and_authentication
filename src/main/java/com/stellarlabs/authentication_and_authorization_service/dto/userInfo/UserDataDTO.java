package com.stellarlabs.authentication_and_authorization_service.dto.userInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDataDTO {

    private String uuid;

    private String name;

    private Boolean quiz;

    private Byte question;

}
