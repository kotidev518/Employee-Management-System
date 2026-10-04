package com.practice.employee_ms.controller;

import com.practice.employee_ms.config.SecurityConfig;
import com.practice.employee_ms.dto.employee.*;
import com.practice.employee_ms.jwt.JwtService;
import com.practice.employee_ms.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;
import org.springframework.boot.security.autoconfigure.web.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EmployeeController.class,excludeAutoConfiguration = {
        SecurityAutoConfiguration.class,
        SecurityFilterAutoConfiguration.class
})
@Import(SecurityConfig.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService employeeService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser(username = "user@test.com", roles = "USER")
    void getEmployees_shouldReturn200() throws Exception {

        EmployeeResponse response = new EmployeeResponse();
        response.setId(1);
        response.setFirstName("John");
        response.setLastName("Doe");
        response.setEmail("john@example.com");

        Page<EmployeeResponse> page =
                new PageImpl<>(List.of(response));

        when(employeeService.getEmployees(
                org.mockito.ArgumentMatchers.anyString(),
                any()
        )).thenReturn(page);

        mockMvc.perform(
                        get("/employees")
                                .param("search", "")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk());
    }

    @Test
    void getEmployees_withoutAuthentication_shouldReturn401() throws Exception {

        mockMvc.perform(
                        get("/employees")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getEmployees_withSearch_shouldReturn200() throws Exception {

        Page<EmployeeResponse> page =
                new PageImpl<>(List.of());

        when(employeeService.getEmployees(
                eq("john"),
                any(Pageable.class)
        )).thenReturn(page);

        mockMvc.perform(
                        get("/employees")
                                .param("search", "john")
                                .param("page", "0")
                                .param("size", "10")
                )
                .andExpect(status().isOk());

        verify(employeeService).getEmployees(
                eq("john"),
                any(Pageable.class)
        );
    }

    @Test
    @WithMockUser(roles = "USER")
    void getEmployeeById_shouldReturn200() throws Exception {

        EmployeeUserResponse response = new EmployeeUserResponse();
//        response.setId(1);
//        response.setFirstName("John");
//        response.setLastName("Doe");
//        response.setEmail("john@example.com");

        when(employeeService.getEmployeeById(1))
                .thenReturn(response);

        mockMvc.perform(
                        get("/employee/1")
                )
                .andExpect(status().isOk());

        verify(employeeService).getEmployeeById(1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createEmployee_shouldReturn200() throws Exception {

        CreateEmployeeRequest request = new CreateEmployeeRequest();

        EmployeeResponse response = new EmployeeResponse();

        when(employeeService.createEmployee(any(CreateEmployeeRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/employee")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "firstname": "John",
                                "lastname": "Doe",
                                "email": "john@example.com",
                                "department": "IT",
                                "salary": 50000
                            }
                            """)
                )
                .andDo(print())
                .andExpect(status().isOk());

        verify(employeeService).createEmployee(any(CreateEmployeeRequest.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void createEmployee_asUser_shouldReturn403() throws Exception {

        mockMvc.perform(
                        post("/employee")   // use your actual POST mapping
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "firstname": "John",
                                "lastname": "Doe",
                                "email": "john@example.com",
                                "department": "IT",
                                "salary": 50000
                            }
                            """)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateEmployee_shouldReturn200() throws Exception {

        EmployeeResponse response = new EmployeeResponse();

        when(employeeService.updateEmployee(
                eq(1),
                any(UpdateEmployeeRequest.class)
        )).thenReturn(response);

        mockMvc.perform(
                put("/employee/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "firstname": "John",
                                "lastname": "Updated",
                                "email": "john@example.com",
                                "department": "IT",
                                "salary": 60000
                            }
                            """)
        ).andExpect(status().isOk());

        verify(employeeService).updateEmployee(
                eq(1),
                any(UpdateEmployeeRequest.class)
        );
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteEmployee_shouldReturn200() throws Exception{

        when(employeeService.deleteEmployee(1))
                .thenReturn("Employee data deleted");

        mockMvc.perform(
                delete("/employee/1")
                        .with(csrf())
        ).andExpect(status().isOk());

        verify(employeeService).deleteEmployee(1);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createEmployee_withInvalidData_shouldReturn400() throws Exception {
        mockMvc.perform(
                post("/employee")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "firstname": "",
                                "lastname": "Doe",
                                "email": "john@example.com",
                                "department": "IT",
                                "salary": -5000
                            }
                            """)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateEmployee_withInvalidData_shouldReturn400() throws  Exception{

        mockMvc.perform(
                put("/employee/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "firstname": "",
                                "lastname": "",
                                "email": "invalid-email",
                                "department": "",
                                "salary": -1000
                            }
                            """)
        ).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateEmployee_asUser_shouldReturn403() throws Exception {

        mockMvc.perform(
                        put("/employee/1")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                            {
                                "firstname": "John",
                                "lastname": "Updated",
                                "email": "john@example.com",
                                "department": "IT",
                                "salary": 60000
                            }
                            """)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteEmployee_asUser_shouldReturn403() throws Exception {

        mockMvc.perform(
                        delete("/employee/1")
                                .with(csrf())
                )
                .andExpect(status().isForbidden());
    }
}
