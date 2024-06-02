package com.stellarlabs.authentication_and_authorization_service.dto.password;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ForgotPasswordDto {

    @Email(regexp = "^[a-zA-Z0-9_+.-]+[a-zA-Z0-9]@[a-zA-Z0-9][a-zA-Z0-9.-]+\\..[a-zA-Z]{1,}$", message = "is not valid:(")
    @Length(max = 100, message = "maximum can contain 100 symbols")
    private String email;

}
