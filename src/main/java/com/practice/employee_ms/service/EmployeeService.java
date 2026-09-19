package com.practice.employee_ms.service;

import com.practice.employee_ms.dto.employee.CreateEmployeeRequest;
import com.practice.employee_ms.dto.employee.EmployeeResponse;
import com.practice.employee_ms.dto.employee.UpdateEmployeeRequest;
import com.practice.employee_ms.exception.EmployeeNotFoundException;
import com.practice.employee_ms.mapper.EmployeeMapper;
import com.practice.employee_ms.model.Employee;
import com.practice.employee_ms.repo.EmployeeRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository empRepo;
    private final EmployeeMapper employeeMapper;

    public List<EmployeeResponse> getEmployees() {
//        List<Employee> employees= empRepo.findAll();
//        return  employees;
        return empRepo.findAll()
                .stream()
                .map(employeeMapper::toResponse)
                .toList();
    }

    public Object getEmployeeById(int id) {
        Employee employee= empRepo.findById(id)
                .orElseThrow(
                        ()-> new EmployeeNotFoundException("Employee not found")
                );

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        assert authentication != null;
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> Objects.equals(auth.getAuthority(), "ROLE_ADMIN"));
        if(isAdmin){
            return employeeMapper.mapToAdminResponse(employee);
        }
        return employeeMapper.mapToUserResponse(employee);
    }



    @Transactional
//    @Secured("ROLE_ADMIN")
    public EmployeeResponse createEmployee(@Valid CreateEmployeeRequest request) {

        Employee employee = new Employee();

        employee.setFirstname(request.getFirstname());
        employee.setLastname(request.getLastname());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setSalary(request.getSalary());

        Employee empData = empRepo.save(employee);

        EmployeeResponse response = new EmployeeResponse();

        response.setId(empData.getId());
        response.setFirstName(empData.getFirstname());
        response.setLastName(empData.getLastname());
        response.setEmail(empData.getEmail());
        response.setDepartment(empData.getDepartment());
        response.setSalary(empData.getSalary());

        return response;


    }

    @Transactional
//    @Secured("ROLE_ADMIN")
    public EmployeeResponse updateEmployee(int id, UpdateEmployeeRequest request) {
        Employee emp  = empRepo.findById(id)
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
        response.setFirstName(emp.getFirstname());
        response.setLastName(emp.getLastname());
        response.setEmail(emp.getEmail());
        response.setDepartment(emp.getDepartment());
        response.setSalary(emp.getSalary());

        return response;
    }


//    @Secured("ROLE_ADMIN")
    @Transactional
    public String deleteEmployee(int id) {
        Employee emp = empRepo.findById(id)
                .orElseThrow(
                        ()-> new EmployeeNotFoundException("Employee not found with id"+ id)
                );
         empRepo.deleteById(id);
         return "Employee Data deleted";
    }
}