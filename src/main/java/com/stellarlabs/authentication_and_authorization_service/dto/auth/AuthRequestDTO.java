package com.stellarlabs.authentication_and_authorization_service.dto.auth;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
/** This DTO is for login, that receive login and password*/
public class AuthRequestDTO {

    @NotBlank
//    @Email(regexp = "^[a-zA-Z0-9_+.-]+[a-zA-Z0-9]@[a-zA-Z0-9][a-zA-Z0-9.-]+\\..[a-zA-Z]{1,}$", message = "is not valid:(")
    @Length(max = 100, message = "maximum can contain 100 symbols")
    private String email;

    @NotBlank
//    @Pattern(regexp = "^(?!.*[\\s\\\"'])(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[`~#?!@$%^&*=;,.+(\\)/[\\\\]{}_-]).{8,}$",
//            message = "should have at least 8 character, 1 uppercase, 1 lowercase, 1 special character (no blank space, quotation marks (’ “), 1 number")
    @Length(max = 100, message = "maximum can contain 100 symbols")
    private String password;

    @Override
    public String toString() {
        return "AuthRequestDTO{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
