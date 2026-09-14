package com.practice.employee_ms.dto.employee;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmployeeResponse {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private BigDecimal salary;
}
