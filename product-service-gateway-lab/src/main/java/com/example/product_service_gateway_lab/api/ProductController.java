package com.example.product_service_gateway_lab.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.product_service_gateway_lab.model.Product;

import java.util.Arrays;
import java.util.List;

@RestController
public class ProductController {
	private static final List<Product> PRODUCTS = Arrays.asList(new Product(1L, "Laptop", 999.99),
			new Product(2L, "Mouse", 29.99), new Product(3L, "Keyboard", 79.99));

	@GetMapping("/api/products")
	public List<Product> getProducts() {
		return PRODUCTS;
	}
	
	@GetMapping("/api/empty")
	public List<Product> emptuy() {
		return null;
	}
}