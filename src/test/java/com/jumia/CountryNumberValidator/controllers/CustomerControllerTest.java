//package com.jumia.CountryNumberValidator.controllers;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.web.context.WebApplicationContext;
//
//import com.jumia.CountryNumberValidator.model.Customer;
//import com.jumia.CountryNumberValidator.repo.CustomerRepo;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
//
//import static org.hamcrest.Matchers.hasSize;
//
//public class CustomerControllerTest {
//	
//	private MockMvc mockMvc;
//
//	  @Autowired
//	  WebApplicationContext context;
//
//	  @Autowired
//	  private CustomerRepo customerRepository;
//
//	  @BeforeEach
//	  public void setUp(){
//	    mockMvc = webAppContextSetup(context).build();
//	    customerRepository.save(new Customer(1l, "Uganda Valid Customer", "(256) 714660221"));
//	    customerRepository.save(new Customer(2l, "Cameroon Valid Customer", "(237) 699209115"));
//	    customerRepository.save(new Customer(3l, "Morocco Invalid Customer", "(212) 6617344445"));
//	    customerRepository.save(new Customer(4l, "Mozambique Invalid Customer", "(258) 042423566"));
//	  }
//
//	  @Test
//	  public void testFindAllCustomers() throws Exception {
//	    mockMvc.perform(get("/customers/filter?page=0&size=10"))
//	        .andExpect(status().isOk())
//	        .andExpect(jsonPath("$.totalElements").value("4"))
//	        .andExpect(jsonPath("$.content", hasSize(4)))
//	        .andExpect(jsonPath("$.content[0].name").value("Uganda Valid Customer"))
//	        .andExpect(jsonPath("$.content[0].phone").value("(256) 714660221"))
//	        .andExpect(jsonPath("$.content[0].countryName").value("Uganda"))
//	        .andExpect(jsonPath("$.content[0].valid").value("true"));
//	  }
//
//	  @Test
//	  public void testFindAllCustomersWithNoPaginationInfo() throws Exception {
//	    mockMvc.perform(get("/customers/filter"))
//	        .andExpect(status().isOk())
//	        .andExpect(jsonPath("$.totalElements").value("4"))
//	        .andExpect(jsonPath("$.content", hasSize(4)))
//	        .andExpect(jsonPath("$.content[0].name").value("Uganda Valid Customer"))
//	        .andExpect(jsonPath("$.content[0].phone").value("(256) 714660221"))
//	        .andExpect(jsonPath("$.content[0].countryName").value("Uganda"))
//	        .andExpect(jsonPath("$.content[0].valid").value("true"));
//	  }
//
//	  @Test
//	  public void testFilterCustomersByCountryName() throws Exception {
//	    mockMvc.perform(get("/customers/filter?countryName=Cameroon&page=0&size=10"))
//	        .andExpect(status().isOk())
//	        .andExpect(jsonPath("$.totalElements").value("1"))
//	        .andExpect(jsonPath("$.content", hasSize(1)))
//	        .andExpect(jsonPath("$.content[0].name").value("Cameroon Valid Customer"))
//	        .andExpect(jsonPath("$.content[0].phone").value("(237) 699209115"))
//	        .andExpect(jsonPath("$.content[0].countryName").value("Cameroon"))
//	        .andExpect(jsonPath("$.content[0].valid").value("true"));
//	  }
//
//	  @Test
//	  public void testFilterCustomersByCountryNameWithNoPaginationInfo() throws Exception {
//	    mockMvc.perform(get("/customers/filter?countryName=Cameroon"))
//	        .andExpect(status().isOk())
//	        .andExpect(jsonPath("$.totalElements").value("1"))
//	        .andExpect(jsonPath("$.content", hasSize(1)))
//	        .andExpect(jsonPath("$.content[0].name").value("Cameroon Valid Customer"))
//	        .andExpect(jsonPath("$.content[0].phone").value("(237) 699209115"))
//	        .andExpect(jsonPath("$.content[0].countryName").value("Cameroon"))
//	        .andExpect(jsonPath("$.content[0].valid").value("true"));
//	  }
//
//	  @Test
//	  public void testFilterCustomersByTrueValidity() throws Exception {
//	    mockMvc.perform(get("/customers/filter?state=true&page=0&size=10"))
//	        .andExpect(status().isOk())
//	        .andExpect(jsonPath("$.totalElements").value("2"))
//	        .andExpect(jsonPath("$.content", hasSize(2)))
//	        .andExpect(jsonPath("$.content[0].name").value("Uganda Valid Customer"))
//	        .andExpect(jsonPath("$.content[0].phone").value("(256) 714660221"))
//	        .andExpect(jsonPath("$.content[0].countryName").value("Uganda"))
//	        .andExpect(jsonPath("$.content[0].valid").value("true"));
//	  }
//
//	  @Test
//	  public void testFilterCustomersByTrueValidityWithNoPagintaionInfo() throws Exception {
//	    mockMvc.perform(get("/customers/filter?state=true"))
//	        .andExpect(status().isOk())
//	        .andExpect(jsonPath("$.totalElements").value("2"))
//	        .andExpect(jsonPath("$.content", hasSize(2)))
//	        .andExpect(jsonPath("$.content[0].name").value("Uganda Valid Customer"))
//	        .andExpect(jsonPath("$.content[0].phone").value("(256) 714660221"))
//	        .andExpect(jsonPath("$.content[0].countryName").value("Uganda"))
//	        .andExpect(jsonPath("$.content[0].valid").value("true"));
//	  }
//
//	  @Test
//	  public void testFilterCustomersByFalseValidity() throws Exception {
//	    mockMvc.perform(get("/customers/filter?state=false&page=0&size=10"))
//	        .andExpect(status().isOk())
//	        .andExpect(jsonPath("$.totalElements").value("2"))
//	        .andExpect(jsonPath("$.content", hasSize(2)))
//	        .andExpect(jsonPath("$.content[0].name").value("Morocco Invalid Customer"))
//	        .andExpect(jsonPath("$.content[0].phone").value("(212) 6617344445"))
//	        .andExpect(jsonPath("$.content[0].countryName").value("Morocco"))
//	        .andExpect(jsonPath("$.content[0].valid").value("false"));
//	  }
//
//	  @Test
//	  public void testFilterCustomersByFalseValidityWithNoPaginatonInfo() throws Exception {
//	    mockMvc.perform(get("/customers/filter?state=false"))
//	        .andExpect(status().isOk())
//	        .andExpect(jsonPath("$.totalElements").value("2"))
//	        .andExpect(jsonPath("$.content", hasSize(2)))
//	        .andExpect(jsonPath("$.content[0].name").value("Morocco Invalid Customer"))
//	        .andExpect(jsonPath("$.content[0].phone").value("(212) 6617344445"))
//	        .andExpect(jsonPath("$.content[0].countryName").value("Morocco"))
//	        .andExpect(jsonPath("$.content[0].valid").value("false"));
//	  }
//
//	  @Test
//	  public void testFilterCustomersByCountryNameAndTrueValidity() throws Exception {
//	    mockMvc.perform(get("/customers/filter?countryName=Uganda&state=true&page=0&size=10"))
//	        .andExpect(status().isOk())
//	        .andExpect(jsonPath("$.totalElements").value("1"))
//	        .andExpect(jsonPath("$.content", hasSize(1)))
//	        .andExpect(jsonPath("$.content[0].name").value("Uganda Valid Customer"))
//	        .andExpect(jsonPath("$.content[0].phone").value("(256) 714660221"))
//	        .andExpect(jsonPath("$.content[0].countryName").value("Uganda"))
//	        .andExpect(jsonPath("$.content[0].valid").value("true"));
//	  }
//
//	  @Test
//	  public void testFilterCustomersByCountryNameAndTrueValidityWithNoPaginationInfo() throws Exception {
//	    mockMvc.perform(get("/customers/filter?countryName=Uganda&state=true"))
//	        .andExpect(status().isOk())
//	        .andExpect(jsonPath("$.totalElements").value("1"))
//	        .andExpect(jsonPath("$.content", hasSize(1)))
//	        .andExpect(jsonPath("$.content[0].name").value("Uganda Valid Customer"))
//	        .andExpect(jsonPath("$.content[0].phone").value("(256) 714660221"))
//	        .andExpect(jsonPath("$.content[0].countryName").value("Uganda"))
//	        .andExpect(jsonPath("$.content[0].valid").value("true"));
//	  }
//
//	  @Test
//	  public void testFilterCustomersByCountryNameAndFalseValidity() throws Exception {
//	    mockMvc.perform(get("/customers/filter?countryName=Morocco&state=false&page=0&size=10"))
//	        .andExpect(status().isOk())
//	        .andExpect(jsonPath("$.totalElements").value("1"))
//	        .andExpect(jsonPath("$.content", hasSize(1)))
//	        .andExpect(jsonPath("$.content[0].name").value("Morocco Invalid Customer"))
//	        .andExpect(jsonPath("$.content[0].phone").value("(212) 6617344445"))
//	        .andExpect(jsonPath("$.content[0].countryName").value("Morocco"))
//	        .andExpect(jsonPath("$.content[0].valid").value("false"));
//	  }
//
//	  @Test
//	  public void testFilterCustomersByCountryNameAndFalseValidityWithNoPaginationInfo() throws Exception {
//	    mockMvc.perform(get("/customers/filter?countryName=Morocco&state=false"))
//	        .andExpect(status().isOk())
//	        .andExpect(jsonPath("$.totalElements").value("1"))
//	        .andExpect(jsonPath("$.content", hasSize(1)))
//	        .andExpect(jsonPath("$.content[0].name").value("Morocco Invalid Customer"))
//	        .andExpect(jsonPath("$.content[0].phone").value("(212) 6617344445"))
//	        .andExpect(jsonPath("$.content[0].countryName").value("Morocco"))
//	        .andExpect(jsonPath("$.content[0].valid").value("false"));
//	  }
//
//	  @Test
//	  public void testFilterCustomersByInvalidCountryName() throws Exception {
//	    mockMvc.perform(get("/customers/filter?countryName=Invalid&page=0&size=10"))
//	        .andExpect(status().isBadRequest())
//	        .andExpect(jsonPath("$.exception").value("IllegalArgumentException"))
//	        .andExpect(jsonPath("$.status").value("400"));
//	  }
//
//	  @Test
//	  public void testFilterCustomersByInvalidCountryNameWithNoPaginatonInfo() throws Exception {
//	    mockMvc.perform(get("/customers/filter?countryName=Invalid"))
//	        .andExpect(status().isBadRequest())
//	        .andExpect(jsonPath("$.exception").value("IllegalArgumentException"))
//	        .andExpect(jsonPath("$.status").value("400"));
//	  }
//
//	  @Test
//	  public void testFilterCustomersByInvalidState() throws Exception {
//	    mockMvc.perform(get("/customers/filter?state=invalid&page=0&size=10"))
//	        .andExpect(status().isBadRequest());
//	  }
//
//	  @Test
//	  public void testFilterCustomersByInvalidStateWithNoPaginatonInfo() throws Exception {
//	    mockMvc.perform(get("/customers/filter?state=invalid"))
//	        .andExpect(status().isBadRequest());
//	  }
//
//	  @Test
//	  public void testFilterCustomersByInvalidCountryNameAndState() throws Exception {
//	    mockMvc.perform(get("/customers/filter?countryName=invalid&state=invalid&page=0&size=10"))
//	        .andExpect(status().isBadRequest());
//	  }
//
//	  @Test
//	  public void testFilterCustomersByInvalidCountryNameAndStateWithNoPaginatonInfo() throws Exception {
//	    mockMvc.perform(get("/customers/filter?countryName=Invalid&state=invalid"))
//	        .andExpect(status().isBadRequest());
//	  }
//
//}
