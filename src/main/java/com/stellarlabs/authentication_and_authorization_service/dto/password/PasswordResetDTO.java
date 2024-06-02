package com.stellarlabs.authentication_and_authorization_service.dto.password;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordResetDTO {

    private String token;

    //    private String expirationDate;
    @Length(max = 100, message = "maximum can contain 100 symbols")
    private String oldPassword;

    @NotBlank
    @Pattern(regexp = "^(?!.*[\\s\\\"'])(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[`~#?!@$%^&*=;,.+(\\)/[\\\\]{}_-]).{8,}$",
            message = "should have at least 8 character, 1 uppercase, 1 lowercase, 1 special character (no blank space, quotation marks (’ “), 1 number")
    @Length(max = 100, message = "maximum can contain 100 symbols")
    private String password;

}
