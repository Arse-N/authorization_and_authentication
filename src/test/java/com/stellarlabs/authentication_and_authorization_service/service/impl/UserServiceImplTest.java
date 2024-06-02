//package com.stellarlabs.authentication_and_authorization_service.service.impl;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.stellarlabs.authentication_and_authorization_service.dto.notification.NotificationResponseDto;
//import com.stellarlabs.authentication_and_authorization_service.dto.UuidResponseDto;
//import com.stellarlabs.authentication_and_authorization_service.model.User;
//import com.stellarlabs.authentication_and_authorization_service.model.UserRole;
//import com.stellarlabs.authentication_and_authorization_service.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.*;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.*;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Optional;
//
//import static org.mockito.Mockito.*;
//
//
//@SpringBootTest
//@RequiredArgsConstructor
//class UserServiceImplTest {
//
//
//
//    @InjectMocks
//    UserServiceImpl userService;
//
//    @Mock
//    UserRepository userRepository;
//
//    @Mock
//    RestTemplate restTemplate;
//
//    User user = User.builder()
//            .id(70L)
//            .uuid("7d4bdcdd-a85e-4323-b8df-f647c77d1bf6")
//            .firstName("poxos")
//            .lastName("poxosyan")
//            .email("poxos.poxosyan@gmail.com")
//            .password("12345678")
//            .phone("+3745555555")
//            .passwordToken("30c4ffe8-a74f-4951-8544-f998fb278559")
//            .build();
//
//    UuidResponseDto accountDTO = UuidResponseDto.builder()
//            .uuid("30c4ffe8-a74f-4951-8544-f998fb278559")
//            .build();
//
//    @Value("${account-service.path}")
//    private String baseurl;
//
//    @Value("${notification-service.path}")
//    private String notificationBaseurl;
//
//    @Test
//    public void saveUser() {
//
//        when(userRepository.save(user)).thenReturn(user);
//        // When
//        userService.saveUser(user);
//        // Then
//        Mockito.verify(userRepository,times(1)).save(user);
//    }
//
//    @Test
//    void send() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<UuidResponseDto> entity = new HttpEntity<>(accountDTO, headers);
//        when(restTemplate.getForEntity(baseurl + "/accounts/create",User.class))
//          .thenReturn(new ResponseEntity(entity, HttpStatus.OK));
//
//    }
//
//    @Test
//    void deleteUserById() throws JsonProcessingException {
//
//        doNothing().when(userRepository).delete(user);
//        userService.deleteUserById(user.getUuid());
//        Mockito.verify(userRepository,times(1)).delete(user);
//    }
//
//    @Test
//    void findUserByEmail() {
//        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
//        userService.getUserByEmail(user.getEmail());
//        Mockito.verify(userRepository,times(2)).findByEmail(user.getEmail());
//    }
//
//    @Test
//    void findUserByUuid() {
//        when(userRepository.findByUuid(user.getUuid())).thenReturn(Optional.of(user));
//        userService.getUserByUuid(user.getUuid());
//        Mockito.verify(userRepository,times(2)).findByUuid(user.getUuid());
//    }
//
//    String passwordToken = "30c4ffe8-a74f-4951-8544-f998fb278559";
//    @Test
//    void findUserByPasswordToken() {
//        when(userRepository.findByPasswordToken(passwordToken)).thenReturn(Optional.of(user));
//        userService.getUserByPasswordToken(passwordToken);
//        Mockito.verify(userRepository,times(2)).findByPasswordToken(passwordToken);
//    }
//
//    @Test
//    void getEmail(){
//        NotificationResponseDto notificationResponseDto = NotificationResponseDto.builder()
//                .subject("The Subject")
////                .message("The Message")
//                .email("Poxos.poxosyan@gmail.com")
//                .build();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        HttpEntity<NotificationResponseDto> entity = new HttpEntity<>(notificationResponseDto, headers);
//        when(restTemplate.getForEntity(notificationBaseurl + "/send_text",NotificationResponseDto.class))
//                .thenReturn(new ResponseEntity(entity, HttpStatus.OK));
//    }
//}