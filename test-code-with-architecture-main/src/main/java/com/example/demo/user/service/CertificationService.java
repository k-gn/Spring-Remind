package com.example.demo.user.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.demo.user.service.port.MailSender;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CertificationService {

	private final MailSender mailSender;

	public void certificate(
		String email,
		long userId,
		String certificationCode
	) {
		String title = "Please certify your email address";
		String certificationUrl = generateCertificationUrl(userId, certificationCode);
		String content = "Please click the following link to certify your email address: " + certificationUrl;
		mailSender.send(email, title, content);
	}

	private String generateCertificationUrl(
		long userId,
		String certificationCode
	) {
		return "http://localhost:8080/api/users/" + userId + "/verify?certificationCode=" + certificationCode;
	}
}
