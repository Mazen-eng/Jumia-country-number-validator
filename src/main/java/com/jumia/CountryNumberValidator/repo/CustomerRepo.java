package com.jumia.CountryNumberValidator.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jumia.CountryNumberValidator.model.Customer;

/**
 * 
 * @author Mazen
 * Customer Repo extending JpaRepository to perform database transactions and operations
 */
@Transactional
@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {
	//Find all customers with pagination info
	Page<Customer> findAll(Pageable pageable);
	
	//Find all customers without pagination info
	List<Customer> findAll();
	
	//Find customers that belong to a specific country by using the country code in the phone number column
	//with pagination info
	Page<Customer> findByPhoneStartsWith(@Param("phone") String phone, Pageable pageable);
	
	//Find customers that belong to a specific country by using the country code in the phone number column
	//without pagination info
	List<Customer> findByPhoneStartsWith(@Param("phone") String phone);
}
