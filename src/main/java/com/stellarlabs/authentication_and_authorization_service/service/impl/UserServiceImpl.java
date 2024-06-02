package com.stellarlabs.authentication_and_authorization_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stellarlabs.authentication_and_authorization_service.dto.*;
import com.stellarlabs.authentication_and_authorization_service.dto.account.AccountDTO;
import com.stellarlabs.authentication_and_authorization_service.dto.auth.AuthResponseDTO;
import com.stellarlabs.authentication_and_authorization_service.dto.auth.RegisterDto;
import com.stellarlabs.authentication_and_authorization_service.dto.auth.UserType;
import com.stellarlabs.authentication_and_authorization_service.dto.notification.NotificationResponseDto;
import com.stellarlabs.authentication_and_authorization_service.dto.userInfo.UserDataDTO;
import com.stellarlabs.authentication_and_authorization_service.dto.userNotification.UserNotificationDto;
import com.stellarlabs.authentication_and_authorization_service.email.SendGridService;
import com.stellarlabs.authentication_and_authorization_service.exception.customExceptions.*;
import com.stellarlabs.authentication_and_authorization_service.mapper.RegisterDtoConverter;
import com.stellarlabs.authentication_and_authorization_service.model.Permissions;
import com.stellarlabs.authentication_and_authorization_service.model.User;
import com.stellarlabs.authentication_and_authorization_service.repository.UserRepository;
//import com.stellarlabs.authentication_and_authorization_service.service.KafkaService;
import com.stellarlabs.authentication_and_authorization_service.service.UserService;
import lombok.*;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final SendGridService sendGridService;

    @Value("${front-url}")
    private String frontURL;
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final PermissionService permissionService;

    @Value("${jwt.header}")
    private String header;

//    @Value("${zone.id}")
//    private String zoneId;
//
//    @Value("${date.time.format}")
//    private String timeFormat;

    private static Pattern pattern;
    private Matcher matcher;

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    public UserDTO convert(User user) {
        UserDTO userDTO = UserDTO.builder()
                .phone(user.getPhone())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .uuid(user.getUuid())
                .email(user.getEmail().toLowerCase())
                .build();
        return userDTO;
    }

//    public AccountDTO send(String uuid) {
//        UuidResponseDto uuidDTO = UuidResponseDto.builder()
//                .uuid(uuid)
//                .build();
////        HttpHeaders headers = new HttpHeaders();
////        headers.setContentType(MediaType.APPLICATION_JSON);
////        HttpEntity<UuidResponseDto> entity = new HttpEntity<>(uuidDTO, headers);
////        AccountDTO accountDTO = restTemplate.postForObject(baseurl + "/users/send", entity, AccountDTO.class);
//        return null;
//
//    }

//    @Override
//    public void deleteUserById(String uuid) {
//        if (userRepository.findByUuid(uuid).isPresent()) {
//            User user = userRepository.findByUuid(uuid).get();
//            UuidResponseDto uuidResponseDto = UuidResponseDto.builder()
//                    .uuid(user.getUuid())
//                    .build();
//
//            kafkaService.sendToKafka("deletetopic", uuidResponseDto);
//            user.setDeleted(true);
//            user.setActive(false);
//            userRepository.save(user);
//
//
//        } else {
//            throw new UserNotFoundException("User not found for delete");
//        }
//    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
//        return userOptional.orElse(null);
    }

    @Override
    public User getUserByUuid(String uuid) {
        return userRepository.findByUuid(uuid);

    }

    public User getUserByVerificationCode(String verificationToken) {
        if (verificationToken == null) {
            throw new TokenIsNullException("Verification token is wrong!");
        } else if (userRepository.findByVerificationToken(verificationToken) != null)
            return userRepository.findByVerificationToken(verificationToken);
        else {
            throw new WrongVerificationToken("Your email has been already verified!");
        }
    }

    @Override
    public User getUserByPasswordToken(String passwordToken) {
        return userRepository.findByPasswordToken(passwordToken);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, new HttpServletResponseWrapper((HttpServletResponse) response) {
            public void setHeader(String name, String value) {
                if (!name.equalsIgnoreCase("Allow")) {
                    super.setHeader(name, value);
                }
            }
        });
    }


    @Override
    public void getEmail(User user) throws JsonProcessingException {
        String link = "/admin/billing";
        String subject = "Welcome to Review project";
        NotificationResponseDto notificationResponseDto = mapToNotifDto(user);
        notificationResponseDto.setLink(link);
        notificationResponseDto.setSubject(subject);
        notificationResponseDto.setCode(1003);
//        kafkaService.sendToKafka("notificationtopic", notificationResponseDto);

    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public NotificationResponseDto mapToNotifDto(User user) {
        return NotificationResponseDto.builder()
                .serviceName("Authorization")
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }


    @Override
    public void emailVerification(User user, String token) throws Exception {
        String link = frontURL + "/auth/verify?token=" + token;
        String subject = "Email verification";
        NotificationResponseDto notificationResponseDto = mapToNotifDto(user);
        notificationResponseDto.setLink(link);
        notificationResponseDto.setSubject(subject);
        notificationResponseDto.setCode(1002);
        sendGridService.sendEmail(user.getEmail(), subject, notificationResponseDto);
    }

    @Override
    public void verifyEmail(User user) throws Exception {
        String verifyToken = UUID.randomUUID().toString();
        user.setVerificationToken(verifyToken);
        emailVerification(user,verifyToken);
    }

    @Override
    public void verify(User user) {
        user.setVerificationToken(null);
        user.setVerification(true);
    }

    @Override
    public AuthResponseDTO authResponse(UserDataDTO user, String token) {
        return AuthResponseDTO.builder()
                .token(token)
                .user(user)
                .build();
    }

//    @Override
//    public String dateByZone() {
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(timeFormat);
//        ZonedDateTime zone = ZonedDateTime.now(ZoneId.of(zoneId));
//        return zone.format(dtf);
//    }

    @Override
    public User convert(RegisterDto registerDto) {
        Permissions permissions = permissionService.getpermission(1L);
        User user = new User();
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPhone(registerDto.getPhone().startsWith("+") ? registerDto.getPhone().substring(1) : registerDto.getPhone());
        user.setUuid(UUID.randomUUID().toString());
        user.setPermissions(permissions);
        user.setVerification(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        return user;
    }

    private UserType checkTimeType(String value) {
        try {
            return UserType.valueOf(value);
        } catch (RuntimeException e) {
            throw new NotValidArgumentException(value + " isn't a user type, please select from default values.");
        }
    }

    @Override
    public void sendToUserNotification(String uuid, String message) {
        UserNotificationDto und = new UserNotificationDto();
        und.setMessage(message);
        und.setAccountUuid(uuid);
        und.setDate(LocalDateTime.now());
//        kafkaService.sendToKafka("usernotification", und);
    }

}
