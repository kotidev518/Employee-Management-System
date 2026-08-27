package com.practice.employee_ms.controller;

import com.practice.employee_ms.model.Employee;
import com.practice.employee_ms.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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


    @PostMapping("/employee")
    public Employee SendData(@RequestBody Employee employee){
        return employeeService.senddata(employee);
    }

    @PutMapping("/employee/{id}")
    public Employee UpdateEmployee(@PathVariable int id,@RequestBody Employee employee){
        return employeeService.updateEmployee(id,employee);
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<String> DeleteEmployee(@PathVariable int id){
        String msg= employeeService.deleteEmployee(id);
        return ResponseEntity.ok(msg);
    }
}
