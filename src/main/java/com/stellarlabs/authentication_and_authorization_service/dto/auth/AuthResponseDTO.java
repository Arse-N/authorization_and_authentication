package com.stellarlabs.authentication_and_authorization_service.dto.auth;

import com.stellarlabs.authentication_and_authorization_service.dto.UserDTO;
import com.stellarlabs.authentication_and_authorization_service.dto.userInfo.UserDataDTO;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
/** This DTO receive generated token for authorization*/
public class AuthResponseDTO {

    private String token;

    private UserDataDTO user;

    @Override
    public String toString() {
        return "AuthResponseDTO{" +
                "token='" + token + '\'' +
                ", userDTO=" + user +
                '}';
    }

}
