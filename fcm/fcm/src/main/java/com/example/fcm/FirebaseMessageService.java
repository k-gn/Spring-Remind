package com.example.fcm;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FirebaseMessageService {

	private final FirebaseMessaging firebaseMessaging;

	public String sendMessage(
		String targetToken,
		String title,
		String body
	) throws FirebaseMessagingException {
		Message message = makeMessage(targetToken, title, body);
		return firebaseMessaging.send(message);
	}

	private Message makeMessage(
		String targetToken,
		String title,
		String body
	) {
		Notification notification = Notification
			.builder()
			.setTitle(title)
			.setBody(body)
			.build();

		return Message
			.builder()
			.setNotification(notification)
			.setToken(targetToken)
			.build();
	}
}
