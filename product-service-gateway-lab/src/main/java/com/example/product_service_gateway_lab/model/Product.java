package com.example.product_service_gateway_lab.model;

public class Product {
	private Long id;
	private String name;
	private Double price;

	public Product(Long id, String name, Double price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

//Getters
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Double getPrice() {
		return price;
	}
}
