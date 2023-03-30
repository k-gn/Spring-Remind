package com.example.fcm;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseMessageService {

	@Value("${fcm.certification}")
	private String credential;

	private void firebaseCreateOption() throws IOException {
		FileInputStream refreshToken = new FileInputStream(credential);
		FirebaseOptions options = FirebaseOptions.builder()
			.setCredentials(GoogleCredentials.fromStream(refreshToken))
			.build();

		FirebaseApp.initializeApp(options);
	}
}
