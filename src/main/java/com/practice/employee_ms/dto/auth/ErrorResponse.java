package com.practice.employee_ms.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private Map<String,String> errors;
    private int status;
    private String msg;
    private LocalDateTime timestamp;
}
