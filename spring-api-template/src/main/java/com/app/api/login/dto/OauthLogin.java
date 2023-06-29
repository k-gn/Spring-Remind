package com.app.api.login.dto;

import java.util.Date;

import com.app.global.jwt.dto.JwtToken;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

public class OauthLogin {

	@Getter
	public static class Request {
		private String memberType;
	}

	@Getter
	public static class Response {

		private final String grantType;
		private final String accessToken;
		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
		private final Date accessTokenExpireTime;
		private final String refreshToken;

		@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
		private final Date refreshTokenExpireTime;

		@Builder
		private Response(
			String grantType,
			String accessToken,
			Date accessTokenExpireTime,
			String refreshToken,
			Date refreshTokenExpireTime
		) {
			this.grantType = grantType;
			this.accessToken = accessToken;
			this.accessTokenExpireTime = accessTokenExpireTime;
			this.refreshToken = refreshToken;
			this.refreshTokenExpireTime = refreshTokenExpireTime;
		}

		public static Response of(JwtToken jwtTokenDto) {
			return Response.builder()
				.grantType(jwtTokenDto.getGrantType())
				.accessToken(jwtTokenDto.getAccessToken())
				.accessTokenExpireTime(jwtTokenDto.getAccessTokenExpireTime())
				.refreshToken(jwtTokenDto.getRefreshToken())
				.refreshTokenExpireTime(jwtTokenDto.getRefreshTokenExpireTime())
				.build();
		}

	}

}
