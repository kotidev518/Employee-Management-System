package com.practice.employee_ms.service;

import com.practice.employee_ms.dto.*;
import com.practice.employee_ms.exception.EmployeeNotFoundException;
import com.practice.employee_ms.model.Employee;
import com.practice.employee_ms.repo.EmployeeRepo;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@EnableMethodSecurity
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepo repo;

    public List<Employee> getemployees() {
        List<Employee> employees=repo.findAll();
        return  employees;
//        return repo.findAll()
//                .stream()
//                .map(this::mapToUserResponse)
//                .toList();
    }



    public Object getEmployeeById(int id) {
        Employee employee=repo.findById(id)
                .orElseThrow(
                        ()-> new EmployeeNotFoundException("Employee not found")
                );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assert authentication != null;
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> Objects.equals(auth.getAuthority(), "ROLE_ADMIN"));
        if(isAdmin){
            return mapToAdminResponse(employee);
        }
        return mapToUserResponse(employee);
    }

    private EmployeeUserResponse mapToUserResponse(Employee employee) {
        EmployeeUserResponse response = new EmployeeUserResponse();

        response.setId(employee.getId());
        response.setFirstname(employee.getFirstname());
        response.setLastname(employee.getLastname());
        response.setDepartment(employee.getDepartment());

        return response;
    }

    private EmployeeAdminResponse mapToAdminResponse(Employee employee) {
        EmployeeAdminResponse response = new EmployeeAdminResponse();

        response.setId(employee.getId());
        response.setFirstname(employee.getFirstname());
        response.setLastname(employee.getLastname());
        response.setEmail(employee.getEmail());
        response.setDepartment(employee.getDepartment());
        response.setSalary(employee.getSalary());

        return response;
    }

    @Transactional
    @Secured("ROLE_ADMIN")
    public EmployeeResponse sendData(@Valid CreateEmployeeRequest request) {

        Employee employee = new Employee();

        employee.setFirstname(request.getFirstname());
        employee.setFirstname(request.getLastname());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setSalary(request.getSalary());

        Employee empdata = repo.save(employee);

        EmployeeResponse response = new EmployeeResponse();

        response.setId(empdata.getId());
        response.setFirstname(empdata.getFirstname());
        response.setLastname(empdata.getLastname());
        response.setEmail(empdata.getEmail());
        response.setDepartment(empdata.getDepartment());
        response.setSalary(empdata.getSalary());

        return response;


    }

    @Transactional
    @Secured("ROLE_ADMIN")
    public EmployeeResponse updateEmployee(int id, UpdateEmployeeRequest request) {
        Employee emp  =repo.findById(id)
                            .orElseThrow(
                                        ()->new EmployeeNotFoundException("No Employee found of the specified id " + id)
                            );

        // Copy updated fields
        emp.setFirstname(request.getFirstname());
        emp.setLastname(request.getLastname());
        emp.setEmail(request.getEmail());
        emp.setDepartment(request.getDepartment());
        emp.setSalary(request.getSalary());

        //Employee updateEmpData = repo.save(emp);

        EmployeeResponse response = new EmployeeResponse();

        response.setId(emp.getId());
        response.setFirstname(emp.getFirstname());
        response.setLastname(emp.getLastname());
        response.setEmail(emp.getEmail());
        response.setDepartment(emp.getDepartment());
        response.setSalary(emp.getSalary());

        return response;
    }


    @Secured("ROLE_ADMIN")
    public String deleteEmployee(int id) {
        Employee emp =repo.findById(id)
                .orElseThrow(
                        ()-> new RuntimeException(" Employee not Found")
                );
         repo.deleteById(id);
         return "Employee Data deleted";
    }
}