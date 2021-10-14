package com.jumia.CountryNumberValidator.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jumia.CountryNumberValidator.dto.CustomerDto;
import com.jumia.CountryNumberValidator.exception.InvalidCountryNameException;
import com.jumia.CountryNumberValidator.exception.NoCustomersFoundException;
import com.jumia.CountryNumberValidator.model.Customer;
import com.jumia.CountryNumberValidator.repo.CustomerRepo;
import com.jumia.CountryNumberValidator.service.CustomerServiceImpl;
import com.jumia.CountryNumberValidator.utils.Cache;
import com.jumia.CountryNumberValidator.utils.mapper.MapFromCustomerToDto;

@ExtendWith(SpringExtension.class)
public class CustomerServiceTest {

	  @InjectMocks
	  private CustomerServiceImpl customerService;
	  @Mock
	  private CustomerRepo customerRepo;
		@Mock
	  private MapFromCustomerToDto customerDTOMapper;
	  @Mock
	  Cache cache;

	  @BeforeEach
		private void setup() throws InvalidCountryNameException {
			Mockito.when(customerDTOMapper.mapToDto(getCameroonValidCustomer())).thenReturn(createValidCustomerDto());
			Mockito.when(customerDTOMapper.mapToDto(getCameroonInvalidCustomer())).thenReturn(createInvalidCustomerDto());
			Mockito.when(customerDTOMapper.mapToDto(getUgandaValidCustomer())).thenReturn(createValidCustomerDto());
			Mockito.when(customerDTOMapper.mapToDto(getUgandaInvalidCustomer())).thenReturn(createInvalidCustomerDto());
			Mockito.when(customerDTOMapper.mapToDto(getEthiopiaValidCustomer())).thenReturn(createValidCustomerDto());
			Mockito.when(customerDTOMapper.mapToDto(getEthiopiaInvalidCustomer())).thenReturn(createInvalidCustomerDto());
			Mockito.when(cache.getCodeFromCountryName("Cameroon")).thenReturn("(237)");
		}


	  @Test
	  public void testFindAllCustomersWithPaginationInfo() throws NoCustomersFoundException, InvalidCountryNameException {

	    Pageable pageable = PageRequest.of(0, 6);
	    Page<Customer> customerPage = new PageImpl<>(getAllCustomers(), pageable, 6);

	    Mockito.when(customerRepo.findAll(pageable)).thenReturn(customerPage);

	    Page<CustomerDto> response = customerService.getValidatedCustomersNumbers(0, 6, "all", "all");

	    assertEquals(6L, response.getTotalElements());
	  }


	  @Test
	  public void testFindCustomersByCountryNameWithPaginationInfo() throws NoCustomersFoundException, InvalidCountryNameException {
	    Pageable pageable = PageRequest.of(0, 5);
	    Page<Customer> customerPage = new PageImpl<>(Lists.list(getCameroonValidCustomer(), getCameroonInvalidCustomer()));
	    Mockito.when(customerRepo.findByPhoneStartsWith("(237)", pageable)).thenReturn(customerPage);
	    Page<CustomerDto> response = customerService.getValidatedCustomersNumbers(0, 5, "Cameroon", "all");
	    assertEquals(2L, response.getTotalElements());
	  }

	  @Test
	  public void testFindCustomersByInvalidCountryNameWithPaginationInfo() {

	    Pageable pageable = PageRequest.of(0, 5);
	    Page<Customer> customerPage = new PageImpl<>(Lists.list(getCameroonInvalidCustomer(), getCameroonValidCustomer()));
	    Mockito.when(customerRepo.findByPhoneStartsWith("(273)", pageable)).thenReturn(customerPage);
	    assertThrows(NoCustomersFoundException.class,
	        () -> {
	      customerService.getValidatedCustomersNumbers(0, 5, "invalidCountryName", "all");
	    });
	  }

	  @Test
	  public void testFindCustomersByValidState() throws NoCustomersFoundException, InvalidCountryNameException {

	    Mockito.when(customerRepo.findAll()).thenReturn(getAllCustomers());
	    Page<CustomerDto> response = customerService.getValidatedCustomersNumbers(0, 10, "all", "valid");

	    assertEquals(3L, response.getTotalElements());
	  }

	  @Test
	  public void testFindCustomersByInvalidState() throws NoCustomersFoundException {

		    Mockito.when(customerRepo.findAll()).thenReturn(getAllCustomers());
	    assertThrows(RuntimeException.class,
	        () -> {
	      customerService.getValidatedCustomersNumbers(0, 5, "all", "ThisIsInvalidState");
	    });
	  }

	  @Test
	  public void testFindCustomersByValidStateAndValidCountryName() throws NoCustomersFoundException, InvalidCountryNameException {

		    Mockito.when(customerRepo.findByPhoneStartsWith("(237)")).thenReturn(Lists.list(getCameroonInvalidCustomer(), getCameroonValidCustomer()));
		    Page<CustomerDto> response = customerService.getValidatedCustomersNumbers(0, 5, "Cameroon", "valid");
		    assertEquals(1L, response.getTotalElements());
	  }

	  @Test
	  public void testFindCustomersByInvalidStateAndValidCountryName() throws NoCustomersFoundException {

		    Mockito.when(customerRepo.findByPhoneStartsWith("(237)")).thenReturn(Lists.list(getCameroonInvalidCustomer(), getCameroonValidCustomer()));

	    assertThrows(NoCustomersFoundException.class,
	        () -> {
	      customerService.getValidatedCustomersNumbers(0, 5, "Cameroon", "ThisIsInvalidState");
	    });
	  }

	  @Test
	  public void testFindCustomersByValidStateAndInvalidCountryName() {

	    Mockito.when(customerRepo.findByPhoneStartsWith("(237)")).thenReturn(Lists.list(getCameroonInvalidCustomer(), getCameroonValidCustomer()));

	    assertThrows(NoCustomersFoundException.class,
	        () -> {
	      customerService.getValidatedCustomersNumbers(0, 5, "invalidCountryName", "valid");
	    });
	  }

	  @Test
	  public void testFindCustomersByInvalidStateAndInvalidCountryName() {

	    Mockito.when(customerRepo.findByPhoneStartsWith("(237)")).thenReturn(Lists.list(getCameroonInvalidCustomer(), getCameroonValidCustomer()));
	    assertThrows(NoCustomersFoundException.class,
	        () -> {
	  	      customerService.getValidatedCustomersNumbers(0, 5, "invalidCountryName", "InvalidState");
	    });
	  }

	  private List<Customer> getAllCustomers() {
	    Customer cameroonValidCustomer = getCameroonValidCustomer();
	    Customer cameroonInvalidCustomer = getCameroonInvalidCustomer();
	    Customer ugandaValidCustomer = getUgandaValidCustomer();
	    Customer ugandaInvalidCustomer = getUgandaInvalidCustomer();
	    Customer ethiopiaValidCustomer = getEthiopiaValidCustomer();
	    Customer ethiopiaInvalidCustomer = getEthiopiaInvalidCustomer();
	    return Lists.newArrayList(cameroonValidCustomer, cameroonInvalidCustomer,
	    		ugandaValidCustomer, ugandaInvalidCustomer,
	    		ethiopiaValidCustomer, ethiopiaInvalidCustomer);
	  }

	  private Customer getCameroonValidCustomer() {
		    return createCustomer(1l, "Cameroon Valid Customer", "(237) 673122155");
		  }

	  private Customer getCameroonInvalidCustomer() {
		    return createCustomer(2l, "Cameroon Invalid Customer", "(237) 6780009592");
		  }

	  private Customer getUgandaValidCustomer() {
	    return createCustomer(3l, "Uganda Valid Customer", "(256) 775069443");
	  }

	  private Customer getUgandaInvalidCustomer() {
		    return createCustomer(4l, "Uganda Invalid Customer", "(256) 7503O6263");
		  }

	  private Customer getEthiopiaValidCustomer() {
		    return createCustomer(5l, "Ethiopia Invalid Customer", "(251) 914701723");
		  }

	  private Customer getEthiopiaInvalidCustomer() {
		    return createCustomer(6l, "Ethiopia Invalid Customer", "(251) 9119454961");
		  }


	  private Customer createCustomer(Long id, String name, String phone) {
	    Customer customer = new Customer();
	    customer.setId(id);
	    customer.setName(name);
	    customer.setPhone(phone);
	    return customer;
	  }

	private CustomerDto createValidCustomerDto() {
		return createCustomerDto(true);
	}

	private CustomerDto createInvalidCustomerDto() {
		return createCustomerDto(false);
	}

	private CustomerDto createCustomerDto(boolean valid) {
		CustomerDto customerDto = new CustomerDto();
		customerDto.setValid(valid);
		return customerDto;
	}
}
