package com.example.rest_docs.member;

import static com.example.rest_docs.RestDocsConfiguration.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

import com.example.rest_docs.RestDocsConfiguration;
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
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestParameters(
					parameterWithName("size").optional().description("size"),
					parameterWithName("page").optional().description("page")
				)
			));
	}

	@Test
	void member_get_test() throws Exception {
		mockMvc.perform(
				RestDocumentationRequestBuilders.get("/api/members/{id}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("id").description("Member ID")
				),
				responseFields(
					fieldWithPath("email").description("email"),
					fieldWithPath("name").description("name")
				)
			));
	}

	@Test
	void member_create_test() throws Exception {
		mockMvc.perform(
				post("/api/members")
					.contentType(MediaType.APPLICATION_JSON)
					.content(readJson("/json/member-api/member-create.json"))
			)
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				requestFields(
					fieldWithPath("name").description("name").attributes(field("length", "10")),
					fieldWithPath("email").description("email").attributes(field("length", "30"))
				)
			));
	}

	@Test
	void member_modify_test() throws Exception {
		mockMvc.perform(
				RestDocumentationRequestBuilders.put("/api/members/{id}", 1L)
					.contentType(MediaType.APPLICATION_JSON)
					.content(readJson("/json/member-api/member-modify.json"))
			)
			.andExpect(status().isOk())
			.andDo(restDocs.document(
				pathParameters(
					parameterWithName("id").description("Member ID")
				),
				requestFields(
					fieldWithPath("name").description("name").attributes(field("length", "10"))
				)
			));
	}
}