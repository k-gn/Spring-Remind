package com.example.event.e_2;

public class OrderEvent {
	private String productName;

	public OrderEvent(String productName) {
		this.productName = productName;
	}

	public String getProductName() {
		return productName;
	}
}
