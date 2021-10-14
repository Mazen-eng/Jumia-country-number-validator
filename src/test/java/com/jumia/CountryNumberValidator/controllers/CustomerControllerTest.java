package com.jumia.CountryNumberValidator.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.jumia.CountryNumberValidator.CountryNumberValidatorApplication;
import com.jumia.CountryNumberValidator.model.Customer;
import com.jumia.CountryNumberValidator.repo.CustomerRepo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import static org.hamcrest.Matchers.hasSize;

@SpringBootTest(classes = CountryNumberValidatorApplication.class)
@AutoConfigureMockMvc
public class CustomerControllerTest {

	private MockMvc mockMvc;

	@Autowired
	WebApplicationContext context;

	@Autowired
	private CustomerRepo customerRepository;

	@BeforeEach
	public void setUp(){
		mockMvc = webAppContextSetup(context).build();
		customerRepository.save(new Customer(1l, "Uganda Valid Customer", "(256) 714660221"));
		customerRepository.save(new Customer(2l, "Cameroon Valid Customer", "(237) 699209115"));
		customerRepository.save(new Customer(3l, "Morocco Invalid Customer", "(212) 6617344445"));
		customerRepository.save(new Customer(4l, "Mozambique Invalid Customer", "(258) 042423566"));
	}

	@Test
	public void testGetAllCustomers() throws Exception {
		mockMvc.perform(get("/customers?page=0&size=10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value("4"))
				.andExpect(jsonPath("$.content", hasSize(4)))
				.andExpect(jsonPath("$.content[0].customerName").value("Uganda Valid Customer"))
				.andExpect(jsonPath("$.content[0].phoneNumber").value("(256) 714660221"))
				.andExpect(jsonPath("$.content[0].countryCode").value("(256)"))
				.andExpect(jsonPath("$.content[0].countryName").value("Uganda"))
				.andExpect(jsonPath("$.content[0].valid").value("true"));
	}

	@Test
	public void testGetAllCustomersWithNoPaginationInfo() throws Exception {
		mockMvc.perform(get("/customers"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value("4"))
				.andExpect(jsonPath("$.content", hasSize(4)))
				.andExpect(jsonPath("$.content[0].customerName").value("Uganda Valid Customer"))
				.andExpect(jsonPath("$.content[0].phoneNumber").value("(256) 714660221"))
				.andExpect(jsonPath("$.content[0].countryCode").value("(256)"))
				.andExpect(jsonPath("$.content[0].countryName").value("Uganda"))
				.andExpect(jsonPath("$.content[0].valid").value("true"));
	}

	@Test
	public void testFindCustomersByCountryName() throws Exception {
		mockMvc.perform(get("/customers?countryCategory=Cameroon&page=0&size=10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value("1"))
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(jsonPath("$.content[0].customerName").value("Cameroon Valid Customer"))
				.andExpect(jsonPath("$.content[0].phoneNumber").value("(237) 699209115"))
				.andExpect(jsonPath("$.content[0].countryCode").value("(237)"))
				.andExpect(jsonPath("$.content[0].countryName").value("Cameroon"))
				.andExpect(jsonPath("$.content[0].valid").value("true"));
	}

	@Test
	public void testFindCustomersByCountryNameWithNoPaginationInfo() throws Exception {
		mockMvc.perform(get("/customers?countryCategory=Cameroon"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value("1"))
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(jsonPath("$.content[0].customerName").value("Cameroon Valid Customer"))
				.andExpect(jsonPath("$.content[0].phoneNumber").value("(237) 699209115"))
				.andExpect(jsonPath("$.content[0].countryCode").value("(237)"))
				.andExpect(jsonPath("$.content[0].countryName").value("Cameroon"))
				.andExpect(jsonPath("$.content[0].valid").value("true"));
	}

	@Test
	public void testFindCustomersByStateIsValid() throws Exception {
		mockMvc.perform(get("/customers?state=valid&page=0&size=10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value("2"))
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(jsonPath("$.content[0].customerName").value("Uganda Valid Customer"))
				.andExpect(jsonPath("$.content[0].phoneNumber").value("(256) 714660221"))
				.andExpect(jsonPath("$.content[0].countryCode").value("(256)"))
				.andExpect(jsonPath("$.content[0].countryName").value("Uganda"))
				.andExpect(jsonPath("$.content[0].valid").value("true"));
	}

	@Test
	public void testFindCustomersByStateIsValidWithNoPagintaionInfo() throws Exception {
		mockMvc.perform(get("/customers?state=valid"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value("2"))
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(jsonPath("$.content[0].customerName").value("Uganda Valid Customer"))
				.andExpect(jsonPath("$.content[0].phoneNumber").value("(256) 714660221"))
				.andExpect(jsonPath("$.content[0].countryCode").value("(256)"))
				.andExpect(jsonPath("$.content[0].countryName").value("Uganda"))
				.andExpect(jsonPath("$.content[0].valid").value("true"));
	}

	@Test
	public void testFindCustomersByStateIsInvalid() throws Exception {
		mockMvc.perform(get("/customers?state=invalid&page=0&size=10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value("2"))
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(jsonPath("$.content[0].customerName").value("Morocco Invalid Customer"))
				.andExpect(jsonPath("$.content[0].phoneNumber").value("(212) 6617344445"))
				.andExpect(jsonPath("$.content[0].countryCode").value("(212)"))
				.andExpect(jsonPath("$.content[0].countryName").value("Morocco"))
				.andExpect(jsonPath("$.content[0].valid").value("false"));
	}

	@Test
	public void testFindCustomersByStateIsInvalidWithNoPaginatonInfo() throws Exception {
		mockMvc.perform(get("/customers?state=invalid"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value("2"))
				.andExpect(jsonPath("$.content", hasSize(2)))
				.andExpect(jsonPath("$.content[0].customerName").value("Morocco Invalid Customer"))
				.andExpect(jsonPath("$.content[0].phoneNumber").value("(212) 6617344445"))
				.andExpect(jsonPath("$.content[0].countryCode").value("(212)"))
				.andExpect(jsonPath("$.content[0].countryName").value("Morocco"))
				.andExpect(jsonPath("$.content[0].valid").value("false"));
	}

	@Test
	public void testFindCustomersByCountryNameAndStateIsValid() throws Exception {
		mockMvc.perform(get("/customers?countryCategory=Cameroon&state=valid&page=0&size=10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value("1"))
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(jsonPath("$.content[0].customerName").value("Cameroon Valid Customer"))
				.andExpect(jsonPath("$.content[0].phoneNumber").value("(237) 699209115"))
				.andExpect(jsonPath("$.content[0].countryCode").value("(237)"))
				.andExpect(jsonPath("$.content[0].countryName").value("Cameroon"))
				.andExpect(jsonPath("$.content[0].valid").value("true"));
	}

	@Test
	public void testFindCustomersByCountryNameAndStateIsValidWithNoPaginationInfo() throws Exception {
		mockMvc.perform(get("/customers?countryCategory=Cameroon&state=valid"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value("1"))
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(jsonPath("$.content[0].customerName").value("Cameroon Valid Customer"))
				.andExpect(jsonPath("$.content[0].phoneNumber").value("(237) 699209115"))
				.andExpect(jsonPath("$.content[0].countryCode").value("(237)"))
				.andExpect(jsonPath("$.content[0].countryName").value("Cameroon"))
				.andExpect(jsonPath("$.content[0].valid").value("true"));
	}

	@Test
	public void testFindCustomersByCountryNameAndStateIsInvalid() throws Exception {
		mockMvc.perform(get("/customers?countryCategory=Mozambique&state=invalid&page=0&size=10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value("1"))
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(jsonPath("$.content[0].customerName").value("Mozambique Invalid Customer"))
				.andExpect(jsonPath("$.content[0].phoneNumber").value("(258) 042423566"))
				.andExpect(jsonPath("$.content[0].countryCode").value("(258)"))
				.andExpect(jsonPath("$.content[0].countryName").value("Mozambique"))
				.andExpect(jsonPath("$.content[0].valid").value("false"));
	}

	@Test
	public void testFindCustomersByCountryNameAndStateIsInvalidWithNoPaginationInfo() throws Exception {
		mockMvc.perform(get("/customers?countryCategory=Mozambique&state=invalid"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.totalElements").value("1"))
				.andExpect(jsonPath("$.content", hasSize(1)))
				.andExpect(jsonPath("$.content[0].customerName").value("Mozambique Invalid Customer"))
				.andExpect(jsonPath("$.content[0].phoneNumber").value("(258) 042423566"))
				.andExpect(jsonPath("$.content[0].countryCode").value("(258)"))
				.andExpect(jsonPath("$.content[0].countryName").value("Mozambique"))
				.andExpect(jsonPath("$.content[0].valid").value("false"));
	}
}
