package com.employee.service;

import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.employee.dto.EmployeeTaxResponse;
import com.employee.employeeEntity.Employee;
import com.employee.repo.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	 @Autowired
	    private EmployeeRepository employeeRepository;

	    public Employee saveEmployee(Employee employee) {
	        // Validate employee data if necessary
	        return employeeRepository.save(employee);
	    }

	    public List<Employee> getAllEmployees() {
	        return employeeRepository.findAll();
	    }

	    public double calculateTax(double yearlySalary) {
	        double tax = 0;
	        if (yearlySalary <= 250000) {
	            tax = 0;
	        } else if (yearlySalary <= 500000) {
	            tax = (yearlySalary - 250000) * 0.05;
	        } else if (yearlySalary <= 1000000) {
	            tax = 12500 + (yearlySalary - 500000) * 0.10;
	        } else {
	            tax = 12500 + 50000 + (yearlySalary - 1000000) * 0.20;
	        }
	        return tax;
	    }

	    public double calculateCess(double amount) {
	        if (amount > 2500000) {
	            return (amount - 2500000) * 0.02;
	        }
	        return 0;
	    }

	    public double calculateYearlySalary(Employee employee) {
	        LocalDate doj = employee.getDoj();
	        LocalDate now = LocalDate.now();
	        int monthsWorked = (int) ChronoUnit.MONTHS.between(doj, now);
	        return employee.getSalary() * monthsWorked;
	    }
        @Override
	    public List<EmployeeTaxResponse> getTaxDeductionForCurrentYear() {
	        List<Employee> employees = employeeRepository.findAll();
	        List<EmployeeTaxResponse> responseList = new ArrayList<>();

	        LocalDate now = LocalDate.now();
	        LocalDate startOfFinancialYear = LocalDate.of(now.getYear(), Month.APRIL, 1);
	        LocalDate endOfFinancialYear = LocalDate.of(now.getYear() + 1, Month.MARCH, 31);

	        for (Employee employee : employees) {
	            double yearlySalary = calculateYearlySalary(employee, startOfFinancialYear, endOfFinancialYear);
	            double taxAmount = calculateTax(yearlySalary);
	            double cessAmount = calculateCess(yearlySalary);

	            EmployeeTaxResponse response = new EmployeeTaxResponse(
	                    employee.getEmployeeId(),
	                    employee.getFirstName(),
	                    employee.getLastName(),
	                    yearlySalary,
	                    taxAmount,
	                    cessAmount
	            );
	            responseList.add(response);
	        }
	        return responseList;
	    }
	    
	    private double calculateYearlySalary(Employee employee, LocalDate startOfFinancialYear, LocalDate endOfFinancialYear) {
	        LocalDate doj = employee.getDoj();
	        double monthlySalary = employee.getSalary();

	        // Calculate the number of months the employee has worked in the financial year
	        LocalDate effectiveDOJ = doj.isBefore(startOfFinancialYear) ? startOfFinancialYear : doj;
	        long monthsWorked = ChronoUnit.MONTHS.between(effectiveDOJ, endOfFinancialYear.plusDays(1));

	        // If employee joined after the financial year has ended
	        if (monthsWorked < 0) {
	            monthsWorked = 0;
	        }

	        // Calculate the total salary based on months worked
	        return monthlySalary * monthsWorked;
	    }

}
