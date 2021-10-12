package com.jumia.CountryNumberValidator.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Mazen
 * Model represents Customer's data
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

	@Id
	private Long id;
	private String name;
	private @Getter String phone;

}
