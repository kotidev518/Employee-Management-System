package com.practice.employee_ms.mapper;


import com.practice.employee_ms.dto.employee.EmployeeAdminResponse;
import com.practice.employee_ms.dto.employee.EmployeeResponse;
import com.practice.employee_ms.dto.employee.EmployeeUserResponse;
import com.practice.employee_ms.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public EmployeeUserResponse mapToUserResponse(Employee employee) {
        EmployeeUserResponse response = new EmployeeUserResponse();

        response.setId(employee.getId());
        response.setFirstname(employee.getFirstname());
        response.setLastname(employee.getLastname());
        response.setDepartment(employee.getDepartment());

        return response;
    }

    public EmployeeAdminResponse mapToAdminResponse(Employee employee) {
        EmployeeAdminResponse response = new EmployeeAdminResponse();

        response.setId(employee.getId());
        response.setFirstname(employee.getFirstname());
        response.setLastname(employee.getLastname());
        response.setEmail(employee.getEmail());
        response.setDepartment(employee.getDepartment());
        response.setSalary(employee.getSalary());

        return response;
    }

    public EmployeeResponse toResponse(Employee employee){
        EmployeeResponse response = new EmployeeResponse();

        response.setId(employee.getId());
        response.setFirstName(employee.getFirstname());
        response.setLastName(employee.getLastname());
        response.setEmail(employee.getEmail());
        response.setDepartment(employee.getDepartment());
        response.setSalary(employee.getSalary());

        return response;
    }
}
