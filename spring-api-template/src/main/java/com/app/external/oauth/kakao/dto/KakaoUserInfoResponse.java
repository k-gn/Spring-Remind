package com.app.external.oauth.kakao.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
public class KakaoUserInfoResponse {

	private final String id;

	@JsonProperty("kakao_account")
	private final KakaoAccount kakaoAccount;

	public KakaoUserInfoResponse(
		String id,
		KakaoAccount kakaoAccount
	) {
		this.id = id;
		this.kakaoAccount = kakaoAccount;
	}

	@Getter
	public static class KakaoAccount {

		private final String email;
		private final Profile profile;

		public KakaoAccount(
			String email,
			Profile profile
		) {
			this.email = email;
			this.profile = profile;
		}

		@Getter
		public static class Profile {

			private final String nickname;

			@JsonProperty("thumbnail_image_url")
			private final String thumbnailImageUrl;

			public Profile(
				String nickname,
				String thumbnailImageUrl
			) {
				this.nickname = nickname;
				this.thumbnailImageUrl = thumbnailImageUrl;
			}
		}
	}
}
