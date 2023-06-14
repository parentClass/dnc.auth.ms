package com.dnc.auth.config;

import com.dnc.auth.model.responses.BaseExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@ControllerAdvice
public class AuthenticateExceptionAdvicer {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<BaseExceptionResponse> handleResponseStatusException(ResponseStatusException rse) {
        return new ResponseEntity<>(BaseExceptionResponse.builder()
                .timestamp(Instant.now().toString())
                .status(rse.getBody().getStatus())
                .error(rse.getBody().getTitle())
                .message(rse.getBody().getDetail())
                .build(), rse.getStatusCode());
    }
}
