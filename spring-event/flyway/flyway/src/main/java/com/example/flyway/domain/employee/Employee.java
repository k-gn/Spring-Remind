package com.example.flyway.domain.employee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "bigint(20)")
	private Long id;

	@Column(columnDefinition = "varchar(100)")
	private String name;

	@Column(columnDefinition = "int")
	private Integer age;

	@Column(columnDefinition = "varchar(200)")
	private String email;

	@Column(columnDefinition = "varchar(20)")
	private String phone;

	@Column(columnDefinition = "varchar(200)")
	private String address;
}
