package com.practice.employee_ms.service;

import com.practice.employee_ms.dto.CreateEmployeeRequest;
import com.practice.employee_ms.dto.EmployeeResponse;
import com.practice.employee_ms.dto.UpdateEmployeeRequest;
import com.practice.employee_ms.exception.EmployeeNotFoundException;
import com.practice.employee_ms.model.Employee;
import com.practice.employee_ms.repo.EmployeeRepo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableMethodSecurity
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepo repo;

    public List<Employee> getemployees() {

        List<Employee> employees=repo.findAll();
        return employees;
    }



    public Employee getEmployeeById(int id) {
        return repo.findById(id).orElse(null);
    }

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

        Employee updateEmpData = repo.save(emp);

        EmployeeResponse response = new EmployeeResponse();

        response.setId(updateEmpData.getId());
        response.setFirstname(updateEmpData.getFirstname());
        response.setLastname(updateEmpData.getLastname());
        response.setEmail(updateEmpData.getEmail());
        response.setDepartment(updateEmpData.getDepartment());
        response.setSalary(updateEmpData.getSalary());

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