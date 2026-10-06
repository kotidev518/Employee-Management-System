package com.practice.employee_ms.controller;

import com.practice.employee_ms.dto.employee.CreateEmployeeRequest;
import com.practice.employee_ms.dto.employee.EmployeeDetailsResponse;
import com.practice.employee_ms.dto.employee.EmployeeResponse;
import com.practice.employee_ms.dto.employee.UpdateEmployeeRequest;
import com.practice.employee_ms.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(
            summary = "Get all employees",
            description = "Returns employees with pagination, search, and sorting"
    )
    @GetMapping("/employees")
    public Page<EmployeeResponse> getEmployees(@Parameter(
            description = "Search employees by name, email, or department",
            example = "alice"
    )@RequestParam(required = false) String search, @ParameterObject Pageable pageable){
        return employeeService.getEmployees(search,pageable);
    }

    @Operation(
            summary = "Get employee by ID",
            description = "Returns an employee using their ID"
    )    @GetMapping("/employee/{id}")
    public EmployeeDetailsResponse getEmployeeById(@Parameter(
            description = "Employee ID",
            example = "5"
    )@PathVariable int id){
        return employeeService.getEmployeeById(id);
    }

    @Operation(
            summary = "Create employee",
            description = "Creates a new employee. Requires ADMIN role."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/employee")
    public EmployeeResponse createEmployee(@Valid @RequestBody CreateEmployeeRequest request){
        return employeeService.createEmployee(request);
    }

    @Operation(
            summary = "Update employee",
            description = "Updates an existing employee. Requires ADMIN role."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/employee/{id}")
    public EmployeeResponse updateEmployee(@Parameter(
            description = "Employee ID",
            example = "5"
    )@PathVariable int id,@Valid @RequestBody UpdateEmployeeRequest request){
        return employeeService.updateEmployee(id,request);
    }

    @Operation(
            summary = "Delete employee",
            description = "Deletes an employee. Requires ADMIN role."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/employee/{id}")
    public ResponseEntity<String> deleteEmployee(@Parameter(
            description = "Employee ID",
            example = "5"
    )@PathVariable int id){
        String msg= employeeService.deleteEmployee(id);
        return ResponseEntity.ok(msg);
    }
}
