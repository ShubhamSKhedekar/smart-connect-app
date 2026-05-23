package com.springboot.smartcontactmanager.emailapi.service;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import org.springframework.stereotype.Service;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

@Service
public class EmailService {
	
	private String from = "shubhamskhedekar@gmail.com";
	private String password = "svnwkkaevhnlzfjj";

	public Boolean sendEmailService(String to, String subject, String content) {
		try {
			
			System.out.println("send email method");
			//add spring boot mail dependancy
			
			//set system properties
			String host = "smtp.gmail.com";
			Properties properties = System.getProperties();
			properties.put("mail.smtp.host", host);
			properties.put("mail.smtp.port", 465);
			properties.put("mail.smtp.ssl.enable", "true");
			properties.put("mail.smtp.auth", "true");
			
			//get session for "from" mail authentication
			Session session= Session.getInstance(properties, new Authenticator() {
	
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(from, password);
				}
			});
			
			//configure message
			MimeMessage mimeMessage = new MimeMessage(session);
			mimeMessage.setFrom(new InternetAddress(from));
			mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			mimeMessage.setSubject(subject);
			
			
			//set email content(text) and email attachment
			MimeMultipart mimeMultipart = new MimeMultipart();
			MimeBodyPart mimeBodyPart1 = new MimeBodyPart();
			mimeBodyPart1.setText(content);
			MimeBodyPart mimeBodyPart2 = new MimeBodyPart();
			File file = new File("C:\\Users\\shubham khedekar\\Downloads\\otp-verify.jpg");
			
			mimeBodyPart2.attachFile(file);
			
			mimeMultipart.addBodyPart(mimeBodyPart1);
			mimeMultipart.addBodyPart(mimeBodyPart2);
			
			//not able to send html form of email-content - later will learn
			mimeMessage.setContent(mimeMultipart, "text/html");
			
			
			//set email with Transport
			Transport.send(mimeMessage);
			
			System.out.println("Success: Email sent successfully!!");
			return true;
		
		} catch (Exception e) {
			System.out.println("Error: Email not sent!!");
			e.printStackTrace();
		}
		
		return false;
	}
}

