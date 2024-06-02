package com.stellarlabs.authentication_and_authorization_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stellarlabs.authentication_and_authorization_service.dto.*;
import com.stellarlabs.authentication_and_authorization_service.dto.Payments.PaymentDto;
import com.stellarlabs.authentication_and_authorization_service.dto.account.AccountDTO;
import com.stellarlabs.authentication_and_authorization_service.dto.auth.AuthResponseDTO;
import com.stellarlabs.authentication_and_authorization_service.dto.auth.LogoutResponseDto;
import com.stellarlabs.authentication_and_authorization_service.dto.auth.RegisterDto;
import com.stellarlabs.authentication_and_authorization_service.dto.error.Message;
import com.stellarlabs.authentication_and_authorization_service.dto.auth.AuthRequestDTO;
import com.stellarlabs.authentication_and_authorization_service.dto.password.ForgotPasswordDto;
import com.stellarlabs.authentication_and_authorization_service.dto.password.PasswordResetDTO;
import com.stellarlabs.authentication_and_authorization_service.dto.userInfo.UserDataDTO;
import com.stellarlabs.authentication_and_authorization_service.exception.customExceptions.*;

import com.stellarlabs.authentication_and_authorization_service.model.User;
import com.stellarlabs.authentication_and_authorization_service.model.UserInfo;
import com.stellarlabs.authentication_and_authorization_service.security.CurrentUserDetailServiceImpl;
import com.stellarlabs.authentication_and_authorization_service.security.JwtTokenUtil;
import com.stellarlabs.authentication_and_authorization_service.service.CookieService;
//import com.stellarlabs.authentication_and_authorization_service.service.KafkaService;
import com.stellarlabs.authentication_and_authorization_service.service.PasswordService;
import com.stellarlabs.authentication_and_authorization_service.service.UserService;
import com.stellarlabs.authentication_and_authorization_service.service.impl.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping
public class UserController {

//    @Value("${jwt.header}")
//    private String header;
//
//    private final CurrentUserDetailServiceImpl currentUserDetailService;

//    private final KafkaService kafkaService;

    private final UserService userService;

    private final UserInfoService userInfoService;

    private final PasswordService passwordService;

    private final JwtTokenUtil jwtTokenUtil;

    private final PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper;

//    private final CookieService cookieService;
//
//    @Value("${jwt.password-expiration}")
//    private int expirationTime;

    /**
     * "/register" getting user info from frontEnd,
     * sending email to user for email verification.
     *
     * @param registerDto getting our users info
     * @return {@link  ResponseEntity} HttpStatus created if user successfully saved or throws exception
     * @see <a href="https://docs.oracle.com/en/cloud/paas/content-cloud/rest-api-sites-management/op-requests-id-edit-form-get.html">@RequestBody</a>
     */
    @PostMapping(value = "/register")
    public ResponseEntity<?> saveUser(@Valid @RequestBody RegisterDto registerDto) throws Exception {
        log.info("Registered new user");
        User user = userService.convert(registerDto);
        if (userService.existByEmail(user.getEmail())) {
            throw new AlreadyExistException("Email " + user.getEmail() + " is already used");
        } else {
            userService.verifyEmail(user);
            userService.saveUser(user);
            user = userService.getUserByEmail(registerDto.getEmail());
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(user.getUuid());
            userInfoService.saveUserInfo(userInfo);
            return ResponseEntity.status(HttpStatus.OK).body(new Message(200, "please verify your email address", "Ok"));
        }
    }

    /**
     * "/verify" getting user verification token if exist in DB
     * changing verification status to true.
     *
     * @param mailVerificationDto getting user email verification token
     * @return {@link  ResponseEntity} HttpStatus created if user successfully saved or throws exception
     * @see <a href="https://docs.oracle.com/en/cloud/paas/content-cloud/rest-api-sites-management/op-requests-id-edit-form-get.html">@RequestBody</a>
     */
    @PostMapping(value = "/verify")
    public ResponseEntity<?> verifyUser(@RequestBody MailVerificationDto mailVerificationDto) throws JsonProcessingException {
        log.info("User is verifying email.");
        String token = mailVerificationDto.getToken();
        User user = userService.getUserByVerificationCode(token);
        userService.getEmail(user);
        userService.verify(user);
        userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(new Message(200, "Email is successfully verified.", "Ok"));
    }

    /**
     * "/login" getting user email and password, checking if exist in DB and verified,
     * generating JWT token for authorization and sending to frontEnd with user info
     *
     * @param authRequest getting AuthRequestDTO(which contain email and password) for authorization
     * @return {@link  ResponseEntity} HttpStatus Ok if successfully saved or throws exception
     * @see <a href="https://docs.oracle.com/en/cloud/paas/content-cloud/rest-api-sites-management/op-requests-id-edit-form-get.html">@RequestBody</a>
     * @see <a href="https://docs.oracle.com/javaee/7/api/javax/validation/Valid.html">@Valid</a>
     */

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequestDTO authRequest, HttpServletResponse response) {
        log.info("User is logged in");
        log.info("request -----> " + authRequest.toString());
        User user = userService.getUserByEmail(authRequest.getEmail().toLowerCase());
        if (user != null) {
            if (user.isVerification()) {
                UserInfo userInfo = userInfoService.getUser(user.getUuid());
                if (!userInfo.isPermanentlyBlocked()) {
                    if (!userInfo.isBlocked()) {
                        if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                            String token = jwtTokenUtil.generateToken(user.getEmail());
                            response.setHeader("Authorization", "Bearer " + token);
                            UserDataDTO userDataDTO = userInfoService.getUserData(user);
                            AuthResponseDTO authResponseDTO = userService.authResponse(userDataDTO, token);
                            log.info("response -----> " + authResponseDTO.toString());
                            userInfo.setFailedCount(0);
                            userService.saveUser(user);
                            return ResponseEntity.status(HttpStatus.OK).body(authResponseDTO);
                        } else {
                            userInfo.setFailedCount(userInfo.getFailedCount() + 1);
                            if (userInfo.getFailedCount() == 5) {
                                userInfo.setBlocked(true);
                                userInfo.setBlockTime(LocalDateTime.now().plusHours(1));
                                userInfo.setBlockedCount(userInfo.getBlockedCount() + 1);
                                if (userInfo.getBlockedCount() == 3) {
                                    userInfo.setPermanentlyBlocked(true);
                                }
                            }
                            userInfoService.saveUserInfo(userInfo);
                            userService.saveUser(user);
                            throw new WrongPasswordException("Wrong email or password.");
                        }
                    } else {
                        System.out.println(userInfo.getBlockTime());
                        System.out.println(LocalDateTime.now());
                        if (userInfo.getBlockTime().isBefore(LocalDateTime.now())) {
                            userInfo.setBlockTime(null);
                            userInfo.setBlocked(false);
                            throw new WrongPasswordException("You have been unblocked,try again.");
                        } else {
                            throw new WrongPasswordException("This User is still blocked " + Duration.between(LocalDateTime.now(), userInfo.getBlockTime()).toMinutes() + " minutes");
                        }
                    }
                } else {
                    throw new WrongPasswordException("This User is permanently blocked, Please contact with admin.");
                }
            } else
                throw new NotVerified("Please verify your email!");
        } else {
            throw new UnauthorizedException("Wrong email or password.");
        }
    }

//    @PostMapping("/check_login")
//    public Stream<Cookie> checkLogin(HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//        return Arrays.stream(cookies).filter(
//                cookie -> cookie.getName().equals("jwt")
//        );
//    }

    /**
     * "/me" is sending all user info,also Info ,that saved in Account service, to front.
     * Sending uuid to Account service and then getting all info with that uuid from account service and sending to front
     *
     * @param uuid getting UuidRequestDTO(which contain uuid) sending uuid to Account service
     * @return {@link  ResponseEntity} HttpStatus Ok if jwt token successfully generated or throws exception
     * @see <a href="https://docs.oracle.com/en/cloud/paas/content-cloud/rest-api-sites-management/op-requests-id-edit-form-get.html">@RequestBody</a>
     */

    @PostMapping(value = "/me", produces = "application/json")
    public ResponseEntity<UserDataDTO> sendAccountInfo(@RequestBody UuidResponseDto uuid, HttpServletRequest request) {
        log.info("user getting data from /me");
        log.info("request -----> " + uuid.toString());
        User user = userService.getUserByUuid(uuid.getUuid());
        String token = passwordService.getJwtFromHeader(request);
        log.info("token --------> " + token);
        if (user != null) {
            String email = jwtTokenUtil.getUsernameFromToken(token);
            if (user.getEmail().equals(email)) {
                UserDataDTO userDataDTO = userInfoService.getUserData(user);
                log.info("response -----> " + userDataDTO.toString());
                return ResponseEntity.status(HttpStatus.OK).body(userDataDTO);
            } else {
                throw new PermissionDeniedException("Permission denied");
            }
        } else {
            throw new UuidNotFoundException("User not found");
        }
    }


    /**
     * "/logout" is refreshing jwt token and generating new one.
     *
     * @param request getting Jwt token from front /and changing
     * @return {@link  ResponseEntity} HttpStatus Ok if all user info from account service successfully sending or throws exception
     * @see <a href="https://docs.oracle.com/en/cloud/paas/content-cloud/rest-api-sites-management/op-requests-id-edit-form-get.html">@RequestBody</a>
     */
    @PostMapping(value = "/logout/", produces = "application/json")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("checking JWT token expiration.");
        String token = passwordService.getJwtFromHeader(request);
//        String token = cookieService.getJwtFromCookie(request);
        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
//            cookieService.createCookie(response, token, 0);
            response.setHeader("Authorization", " ");
            return ResponseEntity.status(HttpStatus.OK).body(new Message(200, "user is logged out", "Ok"));
        } else {
            throw new NotValidArgumentException("Unauthorized User");
        }
    }

    /**
     * "/forgot_password" is receiving email sending to email message (with generated link)
     * and then receiving new password and updating old one.
     *
     * @param forgotPasswordDto getting email and then new password, for updating old password.
     * @return {@link  ResponseEntity} HttpStatus Ok if user password successfully updated and sending password token or throws exception.
     */
    @PostMapping(value = "/forgot-password", produces = "application/json")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordDto forgotPasswordDto) throws Exception {
        log.info("forgot password.");
        String token = UUID.randomUUID().toString();
        String email = forgotPasswordDto.getEmail();
        User user = userService.getUserByEmail(email);
        if (user != null) {
            if (user.isVerification()) {
                passwordService.updatePasswordToken(email, token);
                passwordService.getEmail(user, token);
                return ResponseEntity.status(HttpStatus.OK).body(new Message(200, "please check your email", "Ok"));
            } else {
                throw new NotVerified("Email is not verified.");
            }
        } else {
            throw new UserNotFoundException("User is not found.");
        }
    }

    /**
     * "/reset_password" when token is null reseating password with settings,
     * else when token is not null working forgot password and changing password.
     *
     * @param passwordResetDTO getting old password and new password, and updating old one.
     * @return {@link  ResponseEntity} HttpStatus Ok if user password successfully updated.
     * @see <a href="https://docs.oracle.com/en/cloud/paas/content-cloud/rest-api-sites-management/op-requests-id-edit-form-get.html">@RequestBody</a>
     */
    @PostMapping(value = "/reset-password", produces = "application/json")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetDTO passwordResetDTO, HttpServletRequest request) {
        String oldPassword = passwordResetDTO.getOldPassword();
        String token = passwordResetDTO.getToken();
        String password = passwordResetDTO.getPassword();
        if (oldPassword != null && token == null) {
            log.info("updating password by reset password");
            String jwtToken = passwordService.getJwtFromHeader(request);
            log.info("request -----> " + jwtToken);
            if (jwtTokenUtil.canTokenBeRefreshed(jwtToken)) {
                passwordService.updateResetPassword(request, oldPassword, password);
                return ResponseEntity.status(HttpStatus.OK).body(new Message(200, "Password is successfully updated", "Ok"));
            } else {
                throw new TokenExpiredException("Token is expired");
            }
        } else if (oldPassword == null && token != null) {
            log.info("updating password by forgot password");
            passwordService.updateForgotPassword(token, password);
            return ResponseEntity.status(HttpStatus.OK).body(new Message(200, "Password is successfully updated", "OK"));
        } else {
            throw new WrongPasswordException("password is wrong.");
        }
    }

//    /**
//     * "/delete/{id}" is deleting user with id (setting isActive = false, isDeleted = false)
//     *
//     * @param id getting id from url
//     * @return ResponseEntity HttpStatus Ok if user successfully deleted or throws exception
//     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/PathVariable.html">@PathVariable</a>
//     */
//    @DeleteMapping(value = "/delete/{id}", produces = "application/json")
//    public ResponseEntity<?> deleteUser(@PathVariable(value = "id") String id) throws JsonProcessingException {
//        User user = userService.getUserByUuid(id);
//        if (user != null) {
//            userService.deleteUserById(id);
//            return ResponseEntity.status(HttpStatus.OK).body(new Message(200, "User is successfully deleted", "OK"));
//        } else {
//            throw new UserNotFoundException("User not found");
//        }
//    }

    /**
     * "/checkExpiration" is checking jwt token is expired or not
     *
     * @param request getting jwt token
     * @return ResponseEntity HttpStatus Ok if user successfully deleted or throws exception
     * @see <a href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/bind/annotation/PathVariable.html">@PathVariable</a>
     */
    @PostMapping(value = "/checkExpiration")
    public ResponseEntity<Message> checkingExpiration(HttpServletRequest request, HttpServletResponse response) {
        log.info("checking expiration ...");
        String token = passwordService.getJwtFromHeader(request);
        log.info("request -----> " + token);
        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            throw new TokenExpiredException("Token is expired");
        }
    }

}
