package com.practice.employee_ms.service;

import com.practice.employee_ms.dto.employee.CreateEmployeeRequest;
import com.practice.employee_ms.dto.employee.EmployeeAdminResponse;
import com.practice.employee_ms.dto.employee.EmployeeResponse;
import com.practice.employee_ms.dto.employee.UpdateEmployeeRequest;
import com.practice.employee_ms.exception.EmployeeNotFoundException;
import com.practice.employee_ms.mapper.EmployeeMapper;
import com.practice.employee_ms.model.Employee;
import com.practice.employee_ms.repo.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void getEmployees_shouldReturnEmployeeResponses(){
        Employee employee = new Employee();
        employee.setId(1);
        employee.setFirstname("John");
        employee.setLastname("Doe");
        employee.setEmail("john@example.com");
        employee.setDepartment("IT");
        employee.setSalary(new BigDecimal("50000"));

        EmployeeResponse response = new EmployeeResponse();
        response.setId(1);
        response.setFirstName("John");
        response.setLastName("Doe");
        response.setEmail("john@example.com");
        response.setDepartment("IT");
        response.setSalary(new BigDecimal("50000"));

        Pageable pageable = PageRequest.of(0, 10);

        Page<Employee> employeePage =
                new PageImpl<>(List.of(employee));

        when(employeeRepository.findAll(pageable))
                .thenReturn(employeePage);

        when(employeeMapper.toResponse(employee))
                .thenReturn(response);

        Page<EmployeeResponse> result =
                employeeService.getEmployees("",pageable);

        assertNotNull(result);
        assertEquals(1,result.getTotalElements());
        assertEquals(response,result.getContent().get(0));

        verify(employeeRepository).findAll(pageable);
        verify(employeeMapper).toResponse(employee);
    }
    @Test
    void getEmployeeById_shouldReturnAdminResponse() {

        // Arrange
        Employee employee = new Employee();
        employee.setId(1);
        employee.setFirstname("John");
        employee.setLastname("Doe");
        employee.setEmail("john@example.com");
        employee.setDepartment("IT");
        employee.setSalary(new BigDecimal("50000"));

        EmployeeAdminResponse response = new EmployeeAdminResponse();

        when(employeeRepository.findById(1))
                .thenReturn(Optional.of(employee));

        when(employeeMapper.mapToAdminResponse(employee))
                .thenReturn(response);

        Authentication authentication = mock(Authentication.class);

        doReturn(List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN")
        )).when(authentication).getAuthorities();

        SecurityContext securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        // Act
        Object result = employeeService.getEmployeeById(1);

        // Assert
        assertNotNull(result);
        assertEquals(response, result);

        verify(employeeRepository).findById(1);
        verify(employeeMapper).mapToAdminResponse(employee);
    }
    @Test
    void getEmployeeById_shouldThrowExceptionWhenEmployeeNotFound() {

        // Arrange
        when(employeeRepository.findById(99))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeById(99)
        );

        // Verify
        verify(employeeRepository).findById(99);
    }
    @Test
    void createEmployee_shouldCreateAndReturnEmployee() {

        // Arrange
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setFirstname("John");
        request.setLastname("Doe");
        request.setEmail("john@example.com");
        request.setDepartment("IT");
        request.setSalary(new BigDecimal("50000"));

        Employee savedEmployee = new Employee();
        savedEmployee.setId(1);
        savedEmployee.setFirstname("John");
        savedEmployee.setLastname("Doe");
        savedEmployee.setEmail("john@example.com");
        savedEmployee.setDepartment("IT");
        savedEmployee.setSalary(new BigDecimal("50000"));

        EmployeeResponse response = new EmployeeResponse();
        response.setId(1);
        response.setFirstName("John");
        response.setLastName("Doe");
        response.setEmail("john@example.com");
        response.setDepartment("IT");
        response.setSalary(new BigDecimal("50000"));

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(savedEmployee);

        // Act
        EmployeeResponse result = employeeService.createEmployee(request);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("john@example.com", result.getEmail());

        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void updateEmployee_shouldUpdateAndReturnEmployee() {

        // Arrange
        int id = 1;

        UpdateEmployeeRequest request = new UpdateEmployeeRequest();
        request.setFirstname("John Updated");
        request.setLastname("Doe");
        request.setEmail("john.updated@example.com");
        request.setDepartment("Engineering");
        request.setSalary(new BigDecimal("60000"));

        Employee employee = new Employee();
        employee.setId(id);
        employee.setFirstname("John");
        employee.setLastname("Doe");
        employee.setEmail("john@example.com");
        employee.setDepartment("IT");
        employee.setSalary(new BigDecimal("50000"));

        when(employeeRepository.findById(id))
                .thenReturn(Optional.of(employee));

        // Act
        EmployeeResponse result =
                employeeService.updateEmployee(id, request);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("John Updated", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("john.updated@example.com", result.getEmail());
        assertEquals("Engineering", result.getDepartment());
        assertEquals(new BigDecimal("60000"), result.getSalary());

        verify(employeeRepository).findById(id);
    }
    @Test
    void updateEmployee_shouldThrowExceptionWhenEmployeeNotFound() {

        // Arrange
        int id = 99;

        UpdateEmployeeRequest request = new UpdateEmployeeRequest();
        request.setFirstname("John");
        request.setLastname("Doe");
        request.setEmail("john@example.com");
        request.setDepartment("IT");
        request.setSalary(new BigDecimal("50000"));

        when(employeeRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.updateEmployee(id, request)
        );

        // Verify
        verify(employeeRepository).findById(id);
    }
    @Test
    void deleteEmployee_shouldDeleteEmployee() {

        // Arrange
        int id = 1;

        Employee employee = new Employee();
        employee.setId(id);

        when(employeeRepository.findById(id))
                .thenReturn(Optional.of(employee));

        // Act
        String result = employeeService.deleteEmployee(id);

        // Assert
        assertEquals("Employee Data deleted", result);

        verify(employeeRepository).findById(id);
        verify(employeeRepository).deleteById(id);
    }
    @Test
    void deleteEmployee_shouldThrowExceptionWhenEmployeeNotFound() {

        // Arrange
        int id = 99;

        when(employeeRepository.findById(id))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.deleteEmployee(id)
        );

        // Verify
        verify(employeeRepository).findById(id);

        // Make sure delete was NOT called
        verify(employeeRepository, never()).deleteById(id);
    }
}
