package com.dannyjulian.productservice;

import com.dannyjulian.productservice.dto.ProductRequest;
import com.dannyjulian.productservice.repository.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ObjectMapper objectMapper;

	@DynamicPropertySource // DynamicPropertySource facilitates adding properties with dynamic values (at runtime)
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	void createAndGetProduct() throws Exception {

		ProductRequest productRequest = getProductRequest();

		String productRequestStr = objectMapper.writeValueAsString(productRequest);

		// POST TEST
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(productRequestStr))
				.andExpect(status().isCreated());

		Assertions.assertEquals(1, productRepository.findAll().size());


		// GET TEST
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
				.andExpect(status().isOk())
				.andReturn();

		List<ProductRequest> getResponse = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {});
		ProductRequest actualProductRequest = getResponse.get(0);

		Assertions.assertEquals(productRequest.getName(), actualProductRequest.getName());
		Assertions.assertEquals(productRequest.getDescription(), actualProductRequest.getDescription());
		Assertions.assertEquals(productRequest.getPrice(), actualProductRequest.getPrice());
	}

	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("Motorola X")
				.description("Best phone")
				.price(BigDecimal.valueOf(14))
				.build();
	}

}
