package com.app.presentation.web.kakaotoken.dto;

import lombok.Builder;
import lombok.Getter;

public class KakaoToken {

	@Getter
	public static class Request {
		private final String grant_type;
		private final String client_id;
		private final String redirect_uri;
		private final String code;
		private final String client_secret;

		@Builder
		private Request(
			String grant_type,
			String client_id,
			String redirect_uri,
			String code,
			String client_secret
		) {
			this.grant_type = grant_type;
			this.client_id = client_id;
			this.redirect_uri = redirect_uri;
			this.code = code;
			this.client_secret = client_secret;
		}

		public static Request of(
			String client_id,
			String client_secret,
			String code,
			String redirect_uri
		) {
			return Request.builder()
				.client_id(client_id)
				.client_secret(client_secret)
				.grant_type("authorization_code")
				.code(code)
				.redirect_uri(redirect_uri)
				.build();
		}
	}

	@Getter
	public static class Response {
		private final String token_type;
		private final String access_token;
		private final Integer expires_in;
		private final String refresh_token;
		private final Integer refresh_token_expires_in;
		private final String scope;

		@Builder
		public Response(
			String token_type,
			String access_token,
			Integer expires_in,
			String refresh_token,
			Integer refresh_token_expires_in,
			String scope
		) {
			this.token_type = token_type;
			this.access_token = access_token;
			this.expires_in = expires_in;
			this.refresh_token = refresh_token;
			this.refresh_token_expires_in = refresh_token_expires_in;
			this.scope = scope;
		}
	}
}
