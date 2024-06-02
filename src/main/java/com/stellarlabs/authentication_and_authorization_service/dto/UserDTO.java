package com.stellarlabs.authentication_and_authorization_service.dto;

import com.stellarlabs.authentication_and_authorization_service.model.UserRole;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String uuid;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

}
