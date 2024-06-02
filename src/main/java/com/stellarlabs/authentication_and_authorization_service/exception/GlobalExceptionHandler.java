package com.stellarlabs.authentication_and_authorization_service.exception;

import com.stellarlabs.authentication_and_authorization_service.dto.error.ErrorResponse;
import com.stellarlabs.authentication_and_authorization_service.dto.error.Message;
import com.stellarlabs.authentication_and_authorization_service.exception.customExceptions.*;
//import com.stellarlabs.authentication_and_authorization_service.service.KafkaService;
import com.stellarlabs.authentication_and_authorization_service.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;


import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.net.SocketTimeoutException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

//    private final KafkaService kafkaService;

    private final UserService userService;

//    private void sendtokafka(int status, String errType, String message, String uri) {
//        ErrorResponse errorResponse = ErrorResponse.builder()
//                .message(message)
//                .httpStatus(status)
//                .timestamp(OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS)
//                        .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
//                .errorType(errType)
//                .microserviceName("Authorization-And-Authentication")
//                .path(uri)
//                .build();
//
//        kafkaService.sendToKafka("errortopic", errorResponse);
//    }

    private Message responseMessage(int status, String errType, String m) {
        return Message.builder()
                .statusCode(status)
                .status(errType)
                .message(m)
                .build();
    }

    @ExceptionHandler({Throwable.class})
    public ResponseEntity<?> handleException500(Exception exception, HttpServletRequest request) {
//        sendtokafka(500, "Internal Server Error", exception.getLocalizedMessage(), request.getRequestURI());
        Message message = responseMessage(500, "Internal Server Error", exception.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Message> handleException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        String msg = fieldErrors.stream()
                .map(cur -> cur.getField() + " " + cur.getDefaultMessage())
                .collect(Collectors.joining(", "));

//        sendtokafka(400, "Bad Request", msg, request.getRequestURI());
        Message message = responseMessage(400, "Bad Request", msg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler({ResourceAccessException.class})
    public ResponseEntity<Message> handleException503(ResourceAccessException exception, HttpServletRequest request) {
        if (exception.contains(SocketTimeoutException.class)) {
//            sendtokafka(408, "Request Timeout", exception.getCause().getLocalizedMessage(), request.getRequestURI());
            Message message = responseMessage(408, "Request timeout", exception.getCause().getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(message);
        } else {
//            sendtokafka(503, "Service Unavailable", "Account service is unavailable now.", request.getRequestURI());
            Message message = responseMessage(503, "Service Unavailable", "Account service is unavailable now.");
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(message);
        }
    }

    @ExceptionHandler({ConstraintViolationException.class, PasswordValidationException.class, NullPointerException.class,
            EmailNotFoundException.class, WrongVerificationToken.class, IllegalArgumentException.class,
            NotValidArgumentException.class, ExpiredException.class, WrongValidationException.class})
    public ResponseEntity<Message> handleException400(Exception exception, HttpServletRequest request) {
//        sendtokafka(400, "Bad Request", exception.getMessage(), request.getRequestURI());
        Message message = responseMessage(400, "Bad Request", exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<Message> handleExceptionJwt(HttpServletRequest request) {
//        sendtokafka(401, "Bad Request", "This Jwt token is already expired", request.getRequestURI());
        Message message = responseMessage(401, "Bad Request", "This Jwt token is already expired");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<Message> handleExceptionJwt(HttpRequestMethodNotSupportedException exception, HttpServletRequest request) {
//        sendtokafka(405, "Method Not Allowed", exception.getLocalizedMessage(), request.getRequestURI());
        Message message = responseMessage(405, "Method Not Allowed", exception.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(message);
    }

    @ExceptionHandler({PermissionDeniedException.class})
    public ResponseEntity<Message> handleException403(PermissionDeniedException exception, HttpServletRequest request) {
//        sendtokafka(403, "Forbidden", exception.getLocalizedMessage(), request.getRequestURI());
        Message message = responseMessage(403, "Forbidden", exception.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(message);
    }

    @ExceptionHandler({UuidNotFoundException.class, UserNotFoundException.class, NotVerified.class})
    public ResponseEntity<Message> handleException404(Exception exception, HttpServletRequest request) {
//        sendtokafka(404, "Not Found", exception.getLocalizedMessage(), request.getRequestURI());
        Message message = responseMessage(404, "Not Found", exception.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler({UnauthorizedException.class, TokenExpiredException.class, TokenIsNullException.class,
            SignatureException.class, MalformedJwtException.class,  WrongPasswordException.class})
    public ResponseEntity<Message> handleException401(Exception exception, HttpServletRequest request) {
//        sendtokafka(401, "Unauthorized", exception.getLocalizedMessage(), request.getRequestURI());
        Message message = responseMessage(401, "Unauthorized", exception.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
    }

    @ExceptionHandler({AlreadyExistException.class, AlreadyDeletedException.class})
    public ResponseEntity<Message> handleException409(Exception exception, HttpServletRequest request) {
//        sendtokafka(409, "Conflict", exception.getLocalizedMessage(), request.getRequestURI());
        Message message = responseMessage(409, "Conflict", exception.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Message> handleExceptionSql(DataIntegrityViolationException exception, HttpServletRequest request) {
        String msg = exception.getCause().getCause().getMessage();
//        sendtokafka(400, "Bad Request", msg, request.getRequestURI());
        Message message = responseMessage(400, "Bad Request", msg);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Message> handleExceptionDateParse(HttpMessageNotReadableException exception, HttpServletRequest request) {
        String m = "Required request body is missing.";
//        sendtokafka(400, "Bad Request", m, request.getRequestURI());
        Message message = responseMessage(400, "Bad Request", m);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

}
