package com.jumia.CountryNumberValidator.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.jumia.CountryNumberValidator.CountryNumberValidatorApplication;

@SpringBootTest(classes = CountryNumberValidatorApplication.class)
@AutoConfigureMockMvc
public class CountryControllerTest {

	  @Autowired
	  private MockMvc mockMvc;

	  @Autowired
	  WebApplicationContext context;

	  @BeforeEach
	  public void setUp(){
	    mockMvc = webAppContextSetup(context).build();
	  }

	  @Test
	  public void testGetCountriesList() throws Exception {
	    mockMvc.perform(get("/countries"))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$", hasSize(5)));
	  }

}
