package com.practice.employee_ms.dto;

import lombok.Data;

@Data
public class EmployeeUserResponse {
    private Integer id;
    private String firstname;
    private String lastname;
    private String department;
}
