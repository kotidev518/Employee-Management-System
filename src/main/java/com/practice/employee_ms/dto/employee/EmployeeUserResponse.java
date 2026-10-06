package com.practice.employee_ms.dto.employee;

import lombok.Data;

@Data
public class EmployeeUserResponse implements EmployeeDetailsResponse{
    private Integer id;
    private String firstname;
    private String lastname;
    private String department;
}
