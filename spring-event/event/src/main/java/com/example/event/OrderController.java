package com.example.event;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.event.e_2.OrderEventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderController {

	private final OrderEventService orderEventService;

	@GetMapping("/order")
	public ResponseEntity<?> order() {
		orderEventService.order("테스트 제품");
		return ResponseEntity.ok().build();
	}

}
