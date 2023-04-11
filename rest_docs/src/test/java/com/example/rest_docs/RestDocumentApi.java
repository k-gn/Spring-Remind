package com.example.rest_docs;

import java.util.EnumSet;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rest_docs.member.MemberStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("/test")
public class RestDocumentApi {

	private final ObjectMapper objectMapper;

	public RestDocumentApi(ObjectMapper objectMapper) {this.objectMapper = objectMapper;}

	@PostMapping("/sample")
	public void sample(@RequestBody @Valid SampleRequest request) {

	}

	@GetMapping("/memberStatus")
	public ArrayNode getMemberStatus() {
		final ArrayNode arrayNode = objectMapper.createArrayNode();
		final EnumSet<MemberStatus> types = EnumSet.allOf(MemberStatus.class);

		for (final MemberStatus type : types) {
			final ObjectNode objectNode = objectMapper.createObjectNode();
			objectNode.put("MemberStatus", type.name());
			objectNode.put("description", type.getDescription());
			arrayNode.add(objectNode);
		}

		return arrayNode;
	}

	public static class SampleRequest {

		@NotEmpty
		private String name;

		@Email
		private String email;

		public SampleRequest(
			String name,
			String email
		) {
			this.name = name;
			this.email = email;
		}

		public String getName() {
			return name;
		}

		public String getEmail() {
			return email;
		}
	}
}
