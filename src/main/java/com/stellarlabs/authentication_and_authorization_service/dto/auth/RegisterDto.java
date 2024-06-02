package com.stellarlabs.authentication_and_authorization_service.dto.auth;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDto {
    @NotBlank
    @Length(min = 1, max = 100)
    private String firstName;

    @NotBlank
    @Length(min = 1, max = 100)
    private String lastName;


    @NotBlank
    @Pattern(regexp = "^[+]?[0-9]{6,14}$", message = "is not valid:(")
    private String phone;

    @NotBlank
    @Email(regexp = "^[a-zA-Z0-9_+.-]+[a-zA-Z0-9]@[a-zA-Z0-9][a-zA-Z0-9.-]+\\..[a-zA-Z]{1,}$", message = "is not valid:(")
    @Length(max = 100, message = "maximum can contain 100 symbols")
    private String email;

    @NotBlank
    @Pattern(regexp = "^(?!.*[\\s\\\"'])(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[`~#?!@$%^&*=;,.+(\\)/[\\\\]{}_-]).{8,}$",
            message = "should have at least 8 character, 1 uppercase, 1 lowercase, 1 special character (no blank space, quotation marks (’ “), 1 number")
    @Length(max = 100, message = "maximum can contain 100 symbols")
    private String password;

}
