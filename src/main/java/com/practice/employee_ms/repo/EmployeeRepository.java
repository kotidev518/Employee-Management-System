package com.practice.employee_ms.repo;

import com.practice.employee_ms.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeeRepository extends JpaRepository<Employee,Integer> {
}
