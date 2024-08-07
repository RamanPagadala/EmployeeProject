package com.employee.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employee.employeeEntity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
	
   // Employee findByEmployeeId(String employeeId);
}
