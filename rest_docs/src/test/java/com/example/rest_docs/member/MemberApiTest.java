package com.example.rest_docs.member;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
class MemberApiTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void member_page_test() throws Exception {
		mockMvc.perform(
			get("/api/members")
				.param("size", "10")
				.param("page", "0")
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andDo(document("member_page"))
			.andExpect(status().isOk());
	}

	@Test
	void member_get_test() throws Exception {
		mockMvc.perform(
				get("/api/members/{id}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andDo(print())
			.andDo(document("member_get"))
			.andExpect(status().isOk());
	}

	@Test
	void member_create_test() throws Exception {
		mockMvc.perform(
				post("/api/members")
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(MemberSignUpRequest.fixture()))
			)
			.andDo(print())
			.andDo(document("member_create"))
			.andExpect(status().isOk());
	}

	@Test
	void member_modify_test() throws Exception {
		mockMvc.perform(
				put("/api/members/{id}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(MemberModificationRequest.fixture()))
			)
			.andDo(print())
			.andDo(document("member_modify"))
			.andExpect(status().isOk());
	}
}