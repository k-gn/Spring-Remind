package com.example.rest_docs.member;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import com.example.rest_docs.TestSupport;

class MemberApiTest extends TestSupport {

	@Test
	void member_page_test() throws Exception {
		mockMvc.perform(
				get("/api/members")
					.param("size", "10")
					.param("page", "0")
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk());
	}

	@Test
	void member_get_test() throws Exception {
		mockMvc.perform(
				get("/api/members/{id}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk());
	}

	@Test
	void member_create_test() throws Exception {
		mockMvc.perform(
				post("/api/members")
					.contentType(MediaType.APPLICATION_JSON)
					.content(readJson("/json/member-api/member-create.json"))
			)
			.andExpect(status().isOk());
	}

	@Test
	void member_modify_test() throws Exception {
		mockMvc.perform(
				put("/api/members/{id}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
					.content(readJson("/json/member-api/member-modify.json"))
			)
			.andExpect(status().isOk());
	}
}