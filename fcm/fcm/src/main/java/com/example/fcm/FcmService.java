package com.example.fcm;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FcmService {

	public void send() {
		Notification notification = new Notification(title, body);
		Message message = Message.builder()
			.setToken("device_token_here")
			.setNotification(notification)
			.putData("key1", "value1")
			.putData("key2", "value2")
			.build();

		try {
			String response = FirebaseMessaging.getInstance().send(message);
		} catch (FirebaseMessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
