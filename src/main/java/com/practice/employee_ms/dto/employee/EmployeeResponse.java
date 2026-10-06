package com.practice.employee_ms.dto.employee;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmployeeResponse {
    @Schema(description = "Employee ID", example = "5")
    private Integer id;

    @Schema(description = "Employee first name", example = "Alice")
    private String firstName;

    @Schema(description = "Employee last name", example = "Smith")
    private String lastName;

    @Schema(description = "Employee email", example = "alice@example.com")
    private String email;

    @Schema(description = "Employee department", example = "IT")
    private String department;

    @Schema(description = "Employee salary", example = "50000.00")
    private BigDecimal salary;
}
