//package com.jumia.CountryNumberValidator.services;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.assertj.core.util.Lists;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import com.jumia.CountryNumberValidator.exception.NoCustomersFoundException;
//import com.jumia.CountryNumberValidator.model.Customer;
//import com.jumia.CountryNumberValidator.repo.CustomerRepo;
//import com.jumia.CountryNumberValidator.service.CustomerServiceImpl;
//import com.jumia.CountryNumberValidator.utils.Cache;
//import com.jumia.CountryNumberValidator.utils.mapper.MapFromCustomerToDto;
//
//@ExtendWith(SpringExtension.class)
//public class CustomerServiceTest {
//	
//	  @InjectMocks
//	  private CustomerServiceImpl customerService;
//	  @Mock
//	  private CustomerRepo customerRepo;
//	  @Mock
//	  private MapFromCustomerToDto customerDTOMapper;
//	  @Mock
//	  Cache cache;
//
//
//	  @Test
//	  public void testFindAllCustomersWithPaginationInfo() throws NoCustomersFoundException {
//	    
//	    Pageable pageable = PageRequest.of(0, 6);
//	    Page<Customer> customerPage = new PageImpl<>(getAllCustomers(), pageable, 6);
//	    
//	    Mockito.when(customerRepo.findAll(pageable)).thenReturn(customerPage);
//
//	    Map<String, Object> expectedResponseMap = customerService.getValidatedCustomersNumbers(0, 6, "all", "all");
//
//	    assertEquals(6L, expectedResponseMap.get("totalItems"));
//	  }
//	  
//
//	  @Test
//	  public void testFindCustomersByCountryNameWithPaginationInfo() throws NoCustomersFoundException {
//	    
//	    Pageable pageable = PageRequest.of(0, 5);
//	    Page<Customer> customerPage = new PageImpl<>(Lists.list(getCameroonValidCustomer(), getCameroonInvalidCustomer()));
//	    
//	    Mockito.when(customerRepo.findByPhoneStartsWith("(237)", pageable)).thenReturn(customerPage);
//	    Mockito.when(cache.getCodeFromCountryName("Cameroon")).thenReturn("(237)");
//
//	    Map<String, Object> expectedResponseMap = customerService.getValidatedCustomersNumbers(0, 5, "Cameroon", "all");
//
//	    assertEquals(2L, expectedResponseMap.get("totalItems"));
//	  }
//
//	  @Test
//	  public void testFindCustomersByInvalidCountryNameWithPaginationInfo() {
//	    
//	    Pageable pageable = PageRequest.of(0, 5);
//	    Page<Customer> customerPage = new PageImpl<>(Lists.list(getCameroonInvalidCustomer(), getCameroonValidCustomer()));
//	    
//	    Mockito.when(customerRepo.findByPhoneStartsWith("(273)", pageable)).thenReturn(customerPage);
//
//	    assertThrows(NoCustomersFoundException.class,
//	        () -> {
//	      customerService.findCustomersByCountry("invalidCountryName", pageable);
//	    });
//	  }
//
//	  @Test
//	  public void testFindCustomersByInvalidCountryNameWithNoPaginationInfo() {
//	    
//		Pageable pageable = Pageable.unpaged();
//	    Page<Customer> customerPage = new PageImpl<>(Lists.list(getCameroonInvalidCustomer(), getCameroonValidCustomer()));
//	    
//	    Mockito.when(customerRepo.findByPhoneStartsWith("(237)", pageable)).thenReturn(customerPage);
//
//	    assertThrows(NoCustomersFoundException.class,
//	        () -> {
//	      customerService.findCustomersByCountry("invalidCountryName", pageable);
//	    });
//	  }
//	  
//	  @Test
//	  public void testFindCustomersByValidState() throws NoCustomersFoundException {
//	    
//	    Map<String, Object> mockResponse = new HashMap<>();
//	    mockResponse.put("numbers", Lists.list(getCameroonValidCustomer(), getEthiopiaValidCustomer(), getUgandaValidCustomer()));
//	    mockResponse.put("currentPage", 0);
//	    mockResponse.put("totalItems", 3);
//	    mockResponse.put("totalPages", 1);
//	    Mockito.when(customerRepo.findAll()).thenReturn(getAllCustomers());
//	    Mockito.when(customerService.getValidatedCustomersNumbers(0, 5, "all", "valid")).thenReturn(mockResponse);
//
//	    Map<String, Object> expectedResponseMap = customerService.getValidatedCustomersNumbers(0, 5, "all", "valid");
//
//	    assertEquals(6L, expectedResponseMap.get("totalItems"));
//	  }
//	  
//	  @Test
//	  public void testFindCustomersByInvalidState() throws NoCustomersFoundException {
//	    
//		  Map<String, Object> mockResponse = new HashMap<>();
//		    mockResponse.put("numbers", Lists.list(getCameroonValidCustomer(), getEthiopiaValidCustomer(), getUgandaValidCustomer()));
//		    mockResponse.put("currentPage", 0);
//		    mockResponse.put("totalItems", 3);
//		    mockResponse.put("totalPages", 1);
//		    Mockito.when(customerRepo.findAll()).thenReturn(getAllCustomers());
//		    Mockito.when(customerService.getValidatedCustomersNumbers(0, 5, "all", "valid")).thenReturn(mockResponse);
//
//	    assertThrows(IllegalArgumentException.class,
//	        () -> {
//	      customerService.getValidatedCustomersNumbers(0, 5, "all", "ThisIsInvalidState");
//	    });
//	  }
//	  
//	  @Test
//	  public void testFindCustomersByValidStateAndValidCountryName() throws NoCustomersFoundException {
//	    
//		  Map<String, Object> mockResponse = new HashMap<>();
//		    mockResponse.put("numbers", Lists.list(getCameroonValidCustomer()));
//		    mockResponse.put("currentPage", 0);
//		    mockResponse.put("totalItems", 1);
//		    mockResponse.put("totalPages", 1);
//		    Mockito.when(customerRepo.findByPhoneStartsWith("(237)")).thenReturn(Lists.list(getCameroonInvalidCustomer(), getCameroonValidCustomer()));
//		    Mockito.when(customerService.getValidatedCustomersNumbers(0, 5, "Cameroon", "valid")).thenReturn(mockResponse);
//
//		    Map<String, Object> expectedResponseMap = customerService.getValidatedCustomersNumbers(0, 5, "Cameroon", "valid");
//
//		    assertEquals(1L, expectedResponseMap.get("totalItems"));
//	  }
//	  
//	  @Test
//	  public void testFindCustomersByInvalidStateAndValidCountryName() throws NoCustomersFoundException {
//	    
//		  Map<String, Object> mockResponse = new HashMap<>();
//		    mockResponse.put("numbers", Lists.list(getCameroonValidCustomer()));
//		    mockResponse.put("currentPage", 0);
//		    mockResponse.put("totalItems", 1);
//		    mockResponse.put("totalPages", 1);
//		    Mockito.when(customerRepo.findByPhoneStartsWith("(237)")).thenReturn(Lists.list(getCameroonInvalidCustomer(), getCameroonValidCustomer()));
//		    Mockito.when(customerService.getValidatedCustomersNumbers(0, 5, "Cameroon", "valid")).thenReturn(mockResponse);
//
//	    assertThrows(IllegalArgumentException.class,
//	        () -> {
//	      customerService.getValidatedCustomersNumbers(0, 5, "Cameroon", "ThisIsInvalidState");
//	    });
//	  }
//	  
//	  @Test
//	  public void testFindCustomersByValidStateAndInvalidCountryName() {
//
//	    Mockito.when(customerRepo.findByPhoneStartsWith("(237)")).thenReturn(Lists.list(getCameroonInvalidCustomer(), getCameroonValidCustomer()));
//
//	    assertThrows(NoCustomersFoundException.class,
//	        () -> {
//	      customerService.getValidatedCustomersNumbers(0, 5, "invalidCountryName", "valid");
//	    });
//	  }
//	  
//	  @Test
//	  public void testFindCustomersByInvalidStateAndInvalidCountryName() {
//	    	    
//	    Mockito.when(customerRepo.findByPhoneStartsWith("(237)")).thenReturn(Lists.list(getCameroonInvalidCustomer(), getCameroonValidCustomer()));
//	    assertThrows(NoCustomersFoundException.class,
//	        () -> {
//	  	      customerService.getValidatedCustomersNumbers(0, 5, "invalidCountryName", "InvalidState");
//	    });
//	  }
//
//	  private List<Customer> getAllCustomers() {
//	    Customer cameroonValidCustomer = getCameroonValidCustomer();
//	    Customer cameroonInvalidCustomer = getCameroonInvalidCustomer();
//	    Customer ugandaValidCustomer = getUgandaValidCustomer();
//	    Customer ugandaInvalidCustomer = getUgandaInvalidCustomer();
//	    Customer ethiopiaValidCustomer = getEthiopiaValidCustomer();
//	    Customer ethiopiaInvalidCustomer = getEthiopiaInvalidCustomer();
//	    return Lists.newArrayList(cameroonValidCustomer, cameroonInvalidCustomer,
//	    		ugandaValidCustomer, ugandaInvalidCustomer,
//	    		ethiopiaValidCustomer, ethiopiaInvalidCustomer);
//	  }
//	  
//	  private Customer getCameroonValidCustomer() {
//		    return createCustomer(1l, "Cameroon Valid Customer", "(237) 673122155");
//		  }
//	  
//	  private Customer getCameroonInvalidCustomer() {
//		    return createCustomer(2l, "Cameroon Invalid Customer", "(237) 6780009592");
//		  }
//
//	  private Customer getUgandaValidCustomer() {
//	    return createCustomer(3l, "Uganda Valid Customer", "(256) 775069443");
//	  }
//	  
//	  private Customer getUgandaInvalidCustomer() {
//		    return createCustomer(4l, "Uganda Invalid Customer", "(256) 7503O6263");
//		  }
//	  
//	  private Customer getEthiopiaValidCustomer() {
//		    return createCustomer(5l, "Ethiopia Invalid Customer", "(251) 914701723");
//		  }
//	  
//	  private Customer getEthiopiaInvalidCustomer() {
//		    return createCustomer(6l, "Ethiopia Invalid Customer", "(251) 9119454961");
//		  }
//
//
//	  private Customer createCustomer(Long id, String name, String phone) {
//	    Customer customer = new Customer();
//	    customer.setId(id);
//	    customer.setName(name);
//	    customer.setPhone(phone);
//	    return customer;
//	  }
//}
