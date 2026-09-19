package com.practice.employee_ms.dto.employee;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateEmployeeRequest {

    @NotBlank
    @Size(min = 4, max = 20)
    private String firstname;

    @NotBlank
    private String lastname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String department;

    @NotNull
    @Positive
    private BigDecimal salary;
}
