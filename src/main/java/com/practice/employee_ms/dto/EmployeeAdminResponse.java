package com.practice.employee_ms.dto;

import lombok.Data;

@Data
public class EmployeeAdminResponse {
    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String department;
    private double salary;
}
