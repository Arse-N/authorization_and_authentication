//package com.stellarlabs.authentication_and_authorization_service.controller;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.google.i18n.phonenumbers.NumberParseException;
//import com.stellarlabs.authentication_and_authorization_service.dto.*;
//import com.stellarlabs.authentication_and_authorization_service.dto.auth.AuthRequestDTO;
//import com.stellarlabs.authentication_and_authorization_service.dto.auth.RegisterDto;
//import com.stellarlabs.authentication_and_authorization_service.model.User;
//import com.stellarlabs.authentication_and_authorization_service.model.UserRole;
//import com.stellarlabs.authentication_and_authorization_service.security.JwtTokenUtil;
//import com.stellarlabs.authentication_and_authorization_service.service.PasswordService;
//import com.stellarlabs.authentication_and_authorization_service.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import static org.mockito.Mockito.doNothing;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//@RequiredArgsConstructor
//class UserControllerTest {
//
//    @InjectMocks
//    UserController userController;
//
//    RegisterDto registerDto = RegisterDto.builder()
//            .firstName("poxos")
//            .lastName("poxosyan")
//            .email("Arman.naltkanyan@stellar.am")
//            .password("Aa?67sdaass")
//            .phone("+3745555555")
//            .build();
//
//    User user = User.builder()
//            .uuid("7d4bdcdd-a85e-4323-b8df-f647c77d1bf6")
//            .firstName("poxos")
//            .lastName("poxosyan")
//            .email("Arman.naltkanyan@stellar.am")
//            .password("Aa?67sdaass")
//            .phone("+3745555555")
//            .passwordToken("30c4ffe8-a74f-4951-8544-f998fb278559")
//            .build();
//
//    UuidResponseDto uuidResponseDto = UuidResponseDto.builder()
//            .uuid("7d4bdcdd-a85e-4323-b8df-f647c77d1bf6")
//            .build();
//
//    @Mock
//    UserService userService;
//
//    @Mock
//    PasswordService passwordService;
//
//    @Mock
//    JwtTokenUtil jwtTokenUtil;
//
//    @Mock
//    PasswordEncoder passwordEncoder;
//
//    @Test
//    void saveUser() throws JsonProcessingException, NumberParseException {
//        UserDTO userDTO = UserDTO.builder()
//                .uuid("30c4ffe8-a74f-4951-8544-f998fb278559")
//                .firstName("poxos")
//                .lastName("poxosyan")
//                .phone("+3745555555")
//                .email("Arman.naltkanyan@stellar.am")
//                .userRole(UserRole.ADMIN)
//                .build();
////        when(userService.validate(user.getPassword())).thenReturn(true);
//        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
////        when(userService.sendInfo(user)).thenReturn(userDTO);
//        when(passwordEncoder.encode("Aa?67sdaass")).thenReturn(user.getPassword());
//        doNothing().when(userService).saveUser(user);
//        Assertions.assertEquals(userController.saveUser(registerDto).getStatusCode().value(),201);
//    }
//
////    @Test
////    void login()  {
////
////        AuthRequestDTO authRequest = AuthRequestDTO.builder()
////                .email(user.getEmail())
////                .password("Aa?67sdaass")
////                .build();
////        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhcnNlbi5rYXJhcGV0eWFuQHN0ZWxsYXIuYW0iLCJleHAiOjE2NDMwNDQxNDksImlhdCI6MTYzNzA0NDE0OX0.wXenFRMMhvWXrAWZYdy_7ixYiZBWkpCy1b_ybgYbWPH0Eo26-6GAIEGQYmm8cGIrSV88nyZxpWnCk0h0Vsey0Q";
////        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
////        when(passwordEncoder.matches(authRequest.getPassword(), user.getPassword())).thenReturn(true);
////        when(jwtTokenUtil.generateToken(user.getEmail())).thenReturn(token);
////        Assertions.assertEquals(userController.login(authRequest).getStatusCode().value(),200);
////    }
//
////    @Test
////    void sendAccountInfo() throws NotFoundException {
////       AccountDTO accountDTO = AccountDTO.builder()
////               .birthday("sdasd")
////               .city("sadasd")
////                .build();
////        when(userService.getUserByUuid(user.getUuid())).thenReturn(user);
////        when(userService.send(user.getUuid())).thenReturn(accountDTO);
////        Assertions.assertEquals(userController.sendAccountInfo(uuidResponseDto).getStatusCode().value(),200);
////    }
//
////    @Test
////    void forgotPassword() throws MessagingException, UnsupportedEncodingException, JsonProcessingException {
////        ForgotPasswordDto passwordResetDTO = ForgotPasswordDto.builder()
////                .email(user.getEmail())
//////                .token("9dc49c9d-5226-427f-a343-567ea5be02b0")
////                .build();
//////        String resetPasswordLink = "http://localhost:8089/auth/reset_password?token=" + passwordResetDTO.getToken();
////        when(userService.getUserByEmail(user.getEmail())).thenReturn(user);
//////        doNothing().when(passwordService).getEmail(passwordResetDTO.getEmail(), resetPasswordLink);
////        Assertions.assertEquals(userController.forgotPassword(passwordResetDTO).getStatusCode().value(),200);
////    }
//
//    @Test
//    void deleteUser() throws JsonProcessingException {
//        doNothing().when(userService).deleteUserById(user.getUuid());
//        Assertions.assertEquals(userController.deleteUser(user.getUuid()).getStatusCode().value(),200);
//    }
//
//}