package com.practice.employee_ms.controller;

import com.practice.employee_ms.dto.employee.CreateEmployeeRequest;
import com.practice.employee_ms.dto.employee.EmployeeResponse;
import com.practice.employee_ms.dto.employee.UpdateEmployeeRequest;
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
    public List<EmployeeResponse> getEmployees(){
        return employeeService.getEmployees();
    }

    @GetMapping("/employee/{id}")
    public Object getEmployeeById(@PathVariable int id){
        return employeeService.getEmployeeById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/employee")
    public EmployeeResponse createEmployee(@Valid@RequestBody CreateEmployeeRequest request){
        return employeeService.createEmployee(request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/employee/{id}")
    public EmployeeResponse updateEmployee(@PathVariable int id,@Valid @RequestBody UpdateEmployeeRequest request){
        return employeeService.updateEmployee(id,request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/employee/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable int id){
        String msg= employeeService.deleteEmployee(id);
        return ResponseEntity.ok(msg);
    }
}
