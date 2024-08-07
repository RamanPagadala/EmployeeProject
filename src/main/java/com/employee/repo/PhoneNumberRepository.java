package com.employee.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.employee.employeeEntity.PhoneNumber;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
	
	
}
