package com.salad;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class SaladMailSender extends JavaMailSenderImpl {

	public SaladMailSender() {
		final String username = "isyrshopping@gmail.com";
		final String password = "pass123$$";

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.user", username);
		props.put("mail.password", password);

		setJavaMailProperties(props);
		setUsername(username);
		setPassword(password);
	}
}
