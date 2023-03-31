package com.example.fcm;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

@Configuration
public class FirebaseConfig {

	@Value("${fcm.certification}")
	private String credential;

	@Bean
	public FirebaseApp firebaseApp() throws IOException {
		FileInputStream serviceAccountFile = new FileInputStream("src/main/resources/" + credential);
		FirebaseOptions options = FirebaseOptions.builder()
			.setCredentials(GoogleCredentials.fromStream(serviceAccountFile))
			.setProjectId("fcm-test-de540")
			.build();
		return FirebaseApp.initializeApp(options);
	}

	@Bean
	public FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp){
		return FirebaseMessaging.getInstance(firebaseApp);
	}
}
