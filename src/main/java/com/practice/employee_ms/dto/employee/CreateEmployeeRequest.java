package com.practice.employee_ms.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeRequest {

    @Schema(description = "Employee first name", example = "Alice")
    @NotBlank
    @Size(min = 4, max = 20)
    private String firstname;

    @Schema(description = "Employee last name", example = "Smith")
    @NotBlank
    private String lastname;

    @Schema(description = "Employee email", example = "alice@example.com")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "Employee department", example = "IT")
    @NotBlank
    private String department;

    @Schema(description = "Employee salary", example = "50000.00")
    @Positive
    private BigDecimal salary;
}
