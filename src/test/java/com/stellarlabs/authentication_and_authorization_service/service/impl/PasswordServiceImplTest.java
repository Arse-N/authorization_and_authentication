//package com.stellarlabs.authentication_and_authorization_service.service.impl;
//
//import com.stellarlabs.authentication_and_authorization_service.model.User;
//import com.stellarlabs.authentication_and_authorization_service.model.UserRole;
//import com.stellarlabs.authentication_and_authorization_service.repository.UserRepository;
//import com.stellarlabs.authentication_and_authorization_service.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.Optional;
//
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//@RequiredArgsConstructor
//class PasswordServiceImplTest {
//
//    @InjectMocks
//    PasswordServiceImpl passwordService;
//
//    @Mock
//    PasswordEncoder passwordEncoder;
//
//    @Mock
//    UserService userService;
//
//    @Mock
//    UserRepository userRepository;
//
//    User user = User.builder()
//            .id(70L)
//            .uuid("7d4bdcdd-a85e-4323-b8df-f647c77d1bf6")
//            .firstName("poxos")
//            .lastName("poxosyan")
//            .email("Arsen.karapetyan@stellar.am")
//            .password("$2a$10$Yvm.RQn9mGC9HvohypfrYOS4HFKvP02vBvmwHUYqDflnC/Mj7DxTu")
//            .phone("+3745555555")
//            .passwordToken("30c4ffe8-a74f-4951-8544-f998fb278559")
//            .build();
//
//    @Test
//    void updatePasswordToken() {
//
//        String email = "Arman.naltkanyan@stellar.am";
//        String token = "3552c43f-4505-49b7-867c-228088fd58f4";
//        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//        when(userRepository.save(user)).thenReturn(user);
//        passwordService.updatePasswordToken(email,token);
//        Mockito.verify(userRepository,times(2)).findByEmail(user.getEmail());
//        Mockito.verify(userRepository,times(1)).save(user);
//
//    }
//
//    @Test
//    void updatePassword() {
//        when(userRepository.save(user)).thenReturn(user);
//        passwordService.updateResetPassword(user, user.getPasswordToken());
//        Assertions.assertNull(user.getPasswordToken());
//        Mockito.verify(userRepository,times(1)).save(user);
//    }
//
//    @Test
//    void updateOldPassword() {
//        String oldPassword = "12345678";
//        String newPassword ="BArev12?";
//        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//        when(passwordEncoder.encode("12345678")).thenReturn("$2a$10$Yvm.RQn9mGC9HvohypfrYOS4HFKvP02vBvmwHUYqDflnC/Mj7DxTu");
//        when(passwordEncoder.matches(oldPassword, user.getPassword())).thenReturn(true);
//
//        passwordService.updateOldPassword(user.getEmail(), oldPassword, newPassword );
////        Assertions.assertEquals(user.getPassword(),newPassword);
//        Mockito.verify(userRepository,times(2)).findByEmail(user.getEmail());
//    }
//
//}