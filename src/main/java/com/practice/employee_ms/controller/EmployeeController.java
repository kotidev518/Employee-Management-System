package com.practice.employee_ms.controller;

import com.practice.employee_ms.dto.CreateEmployeeRequest;
import com.practice.employee_ms.dto.EmployeeResponse;
import com.practice.employee_ms.dto.UpdateEmployeeRequest;
import com.practice.employee_ms.model.Employee;
import com.practice.employee_ms.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/employees")
    public List<Employee> getEmployees(){
        return employeeService.getemployees();
    }

    @GetMapping("/employee/{id}")
    public Employee getEmployeeById(@PathVariable int id){
        return employeeService.getEmployeeById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/employee")
    public EmployeeResponse CreateEmployee(@Valid@RequestBody CreateEmployeeRequest request){
        return employeeService.sendData(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/employee/{id}")
    public EmployeeResponse UpdateEmployee(@PathVariable int id,@Valid @RequestBody UpdateEmployeeRequest request){
        return employeeService.updateEmployee(id,request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/employee/{id}")
    public ResponseEntity<String> DeleteEmployee(@PathVariable int id){
        String msg= employeeService.deleteEmployee(id);
        return ResponseEntity.ok(msg);
    }
}
