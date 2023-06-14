package com.dnc.auth.model.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseExceptionResponse {
    private Integer status;
    private String error;
    private String message;
    private String timestamp;
}