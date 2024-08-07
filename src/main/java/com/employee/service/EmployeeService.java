package com.employee.service;

import java.util.List;

import com.employee.dto.EmployeeTaxResponse;
import com.employee.employeeEntity.Employee;

public interface EmployeeService {

	public Employee saveEmployee(Employee employee);

	public List<Employee> getAllEmployees();

	public List<EmployeeTaxResponse> getTaxDeductionForCurrentYear();

}
