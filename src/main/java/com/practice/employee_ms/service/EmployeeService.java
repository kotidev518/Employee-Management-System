package com.practice.employee_ms.service;

import com.practice.employee_ms.model.Employee;
import com.practice.employee_ms.repo.EmployeeRepo;
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
    public Employee senddata(Employee employee) {
        return repo.save(employee);
    }

    @Secured("ROLE_ADMIN")
    public Employee updateEmployee(int id, Employee employee) {
        Employee em=repo.findById(id).orElseThrow(()->new RuntimeException("No data found of the specified id"));

        // Copy updated fields
        em.setFirstname(employee.getFirstname());
        em.setLastname(employee.getLastname());
        em.setEmail(employee.getEmail());
        em.setDepartment(employee.getDepartment());
        em.setSalary(employee.getSalary());
        return repo.save(em);
    }
    @Secured("ROLE_ADMIN")
    public String deleteEmployee(int id) {
        Employee emp =repo.findById(id).orElse(null);
         repo.deleteById(id);
         return "Employee Data deleted";
    }
}
