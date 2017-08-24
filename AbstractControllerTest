package com.tii.kb.controller;


import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.WebApplicationContext;

import com.tii.kb.TestKBServiceAppContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestKBServiceAppContext.class)
@WebIntegrationTest({"server.port:8081", "eureka.client.enabled:false"})
@Transactional
public abstract class AbstractControllerTest {
	
	protected MockMvc mvc;

	@Autowired
	protected WebApplicationContext context;

	@Before
	public void setUp() throws Exception {
		//MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.webAppContextSetup(context).build();

		before();
	}

	/**
	 *
	 * @param uri
	 * @return
	 */
	protected MockHttpServletRequestBuilder buildRequest(RequestMethod method, String uri) {

		MockHttpServletRequestBuilder builder;

		switch (method){
			case DELETE:
				builder = MockMvcRequestBuilders
						.delete(uri);
				break;
			case POST:
				builder = MockMvcRequestBuilders
						.post(uri);
				break;
			case PUT:
				builder = MockMvcRequestBuilders
						.put(uri);
				break;
			default:// GET falls into here
				builder = MockMvcRequestBuilders
						.get(uri);
		}

		MockHttpServletRequestBuilder requestBuilder = builder;

		return requestBuilder;
	}

	abstract public void before() throws Exception;

}
