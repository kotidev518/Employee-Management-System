package com.practice.employee_ms.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateEmployeeRequest {

    @NotEmpty
    @Size(min = 4, max = 20)
    private String firstname;

    @NotEmpty
    private String lastname;

    @NotEmpty
    @Email
    private String email;

    @NotBlank
    private String department;

    @Positive
    private Integer salary;
}
