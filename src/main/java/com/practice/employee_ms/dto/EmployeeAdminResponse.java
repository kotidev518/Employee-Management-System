package com.practice.employee_ms.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EmployeeAdminResponse {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String department;
    private BigDecimal salary;
}
